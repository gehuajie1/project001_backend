package com.couple.space.controller;

import com.couple.space.dto.ApiResponse;
import com.couple.space.dto.AnniversaryDTO;
import com.couple.space.dto.NextAnniversaryDTO;
import com.couple.space.entity.Anniversary;
import com.couple.space.entity.User;
import com.couple.space.mapper.AnniversaryMapper;
import com.couple.space.mapper.UserMapper;
import com.couple.space.security.JwtTokenProvider;
import com.couple.space.service.AnniversaryService;
import com.couple.space.common.ResponseHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 * 纪念日控制器
 * 处理纪念日相关的HTTP请求
 */
@Slf4j
@RestController
@RequestMapping("/api/anniversary")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AnniversaryController {
    private final AnniversaryService anniversaryService;
    private final AnniversaryMapper anniversaryMapper;
    private final UserMapper userMapper;
    private final JwtTokenProvider jwtTokenProvider;

    public AnniversaryController(
        AnniversaryService anniversaryService,
        AnniversaryMapper anniversaryMapper,
        UserMapper userMapper,
        JwtTokenProvider jwtTokenProvider
    ) {
        this.anniversaryService = anniversaryService;
        this.anniversaryMapper = anniversaryMapper;
        this.userMapper = userMapper;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * 获取纪念日列表
     * @return 纪念日列表
     */
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<Anniversary>>> getAnniversaryList() {
        log.info("获取纪念日列表");
        try {
            List<Anniversary> anniversaries = anniversaryService.getAllAnniversaries();
            return ResponseHandler.success(anniversaries);
        } catch (Exception e) {
            log.error("获取纪念日列表失败: {}", e.getMessage());
            return ResponseHandler.fail("获取纪念日列表失败：" + e.getMessage());
        }
    }

    /**
     * 添加纪念日
     * @param anniversary 纪念日信息
     * @return 添加结果
     */
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Anniversary>> addAnniversary(@RequestBody Anniversary anniversary, HttpServletRequest request) {
        log.info("添加纪念日: {}", anniversary.getName());
        try {
            // 验证必要字段
            if (anniversary.getName() == null || anniversary.getName().trim().isEmpty()) {
                return ResponseHandler.fail("纪念日名称不能为空");
            }
            if (anniversary.getDate() == null) {
                return ResponseHandler.fail("纪念日日期不能为空");
            }

            // 从请求头中获取token
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                // 从token中获取用户名
                String username = jwtTokenProvider.getUsernameFromToken(token);
                // 根据用户名查询用户ID
                User user = userMapper.findByUsername(username);
                if (user != null) {
                    // 设置用户ID
                    anniversary.setUserId(user.getId());
                    // 设置创建时间和更新时间
                    LocalDate now = LocalDate.now();
                    anniversary.setCreatedAt(now);
                    anniversary.setUpdatedAt(now);
                    // 保存纪念日
                    anniversaryMapper.insert(anniversary);
                    return ResponseHandler.success(anniversary);
                }
            }
            return ResponseHandler.fail("用户未登录");
        } catch (Exception e) {
            log.error("添加纪念日失败: {}", e.getMessage());
            return ResponseHandler.fail("添加纪念日失败：" + e.getMessage());
        }
    }

    /**
     * 获取纪念日详情
     * @param id 纪念日ID
     * @return 纪念日详情
     */
    @GetMapping("/detail/{id}")
    public ResponseEntity<ApiResponse<Anniversary>> getAnniversaryDetail(@PathVariable Long id) {
        log.info("获取纪念日详情: {}", id);
        try {
            Anniversary anniversary = anniversaryService.getAnniversaryById(id);
            if (anniversary == null) {
                return ResponseHandler.fail("纪念日不存在");
            }
            return ResponseHandler.success(anniversary);
        } catch (Exception e) {
            log.error("获取纪念日详情失败: {}", e.getMessage());
            return ResponseHandler.fail("获取纪念日详情失败：" + e.getMessage());
        }
    }

    /**
     * 删除纪念日
     * @param id 纪念日ID
     * @return 删除结果
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAnniversary(@PathVariable Long id) {
        log.info("删除纪念日: {}", id);
        try {
            anniversaryService.deleteAnniversary(id);
            return ResponseHandler.success(null);
        } catch (Exception e) {
            log.error("删除纪念日失败: {}", e.getMessage());
            return ResponseHandler.fail("删除纪念日失败：" + e.getMessage());
        }
    }

    @GetMapping("/next")
    public ResponseEntity<ApiResponse<NextAnniversaryDTO>> getNextAnniversary() {
        log.info("获取下一个纪念日");
        
        List<Anniversary> anniversaries = anniversaryService.getAllAnniversaries();
        if (anniversaries.isEmpty()) {
            log.info("没有设置任何纪念日");
            return ResponseHandler.success(null);
        }

        LocalDate today = LocalDate.now();
        Anniversary nextAnniversary = null;
        long minDaysUntil = Long.MAX_VALUE;

        for (Anniversary anniversary : anniversaries) {
            LocalDate anniversaryDate = anniversary.getDate();
            LocalDate thisYearAnniversary = LocalDate.of(today.getYear(), anniversaryDate.getMonth(), anniversaryDate.getDayOfMonth());
            
            // 如果今年的纪念日已经过去，计算到明年的纪念日
            if (thisYearAnniversary.isBefore(today)) {
                thisYearAnniversary = thisYearAnniversary.plusYears(1);
            }
            
            long daysUntil = ChronoUnit.DAYS.between(today, thisYearAnniversary);
            
            if (daysUntil < minDaysUntil) {
                minDaysUntil = daysUntil;
                nextAnniversary = anniversary;
            }
        }

        if (nextAnniversary == null) {
            log.info("未能找到下一个纪念日");
            return ResponseHandler.success(null);
        }

        NextAnniversaryDTO dto = new NextAnniversaryDTO();
        dto.setTitle(nextAnniversary.getName());
        dto.setDays(minDaysUntil);
        dto.setState("success");

        log.info("找到下一个纪念日: {}, 距离还有 {} 天", 
            nextAnniversary.getName(), minDaysUntil);
        
        return ResponseHandler.success(dto);
    }
} 