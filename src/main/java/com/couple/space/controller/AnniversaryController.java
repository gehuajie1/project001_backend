package com.couple.space.controller;

import com.couple.space.dto.ApiResponse;
import com.couple.space.dto.AnniversaryDTO;
import com.couple.space.dto.NextAnniversaryDTO;
import com.couple.space.entity.Anniversary;
import com.couple.space.service.AnniversaryService;
import com.couple.space.common.ResponseHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * 纪念日控制器
 * 处理纪念日相关的HTTP请求
 */
@Slf4j
@RestController
@RequestMapping("/api/anniversary")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class AnniversaryController {
    private final AnniversaryService anniversaryService;

    /**
     * 创建纪念日
     * @param anniversary 纪念日对象
     * @return 创建结果
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Anniversary>> createAnniversary(@RequestBody Anniversary anniversary) {
        log.info("收到创建纪念日请求: {}", anniversary.getName());
        try {
            Anniversary created = anniversaryService.createAnniversary(anniversary);
            return ResponseHandler.success(created);
        } catch (Exception e) {
            log.error("创建纪念日失败: {}", e.getMessage());
            return ResponseHandler.fail("创建纪念日失败：" + e.getMessage());
        }
    }

    /**
     * 更新纪念日
     * @param id 纪念日ID
     * @param anniversary 纪念日对象
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Anniversary>> updateAnniversary(
            @PathVariable Long id,
            @RequestBody Anniversary anniversary) {
        log.info("收到更新纪念日请求: {}", id);
        try {
            anniversary.setId(id);
            Anniversary updated = anniversaryService.updateAnniversary(anniversary);
            return ResponseHandler.success(updated);
        } catch (Exception e) {
            log.error("更新纪念日失败: {}", e.getMessage());
            return ResponseHandler.fail("更新纪念日失败：" + e.getMessage());
        }
    }

    /**
     * 删除纪念日
     * @param id 纪念日ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAnniversary(@PathVariable Long id) {
        log.info("收到删除纪念日请求: {}", id);
        try {
            anniversaryService.deleteAnniversary(id);
            return ResponseHandler.success(null);
        } catch (Exception e) {
            log.error("删除纪念日失败: {}", e.getMessage());
            return ResponseHandler.fail("删除纪念日失败：" + e.getMessage());
        }
    }

    /**
     * 获取纪念日详情
     * @param id 纪念日ID
     * @return 纪念日详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Anniversary>> getAnniversary(@PathVariable Long id) {
        log.info("收到获取纪念日详情请求: {}", id);
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
     * 获取用户的所有纪念日
     * @param userId 用户ID
     * @return 纪念日列表
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<Anniversary>>> getAnniversariesByUserId(@PathVariable Long userId) {
        log.info("收到获取用户纪念日列表请求: {}", userId);
        try {
            List<Anniversary> anniversaries = anniversaryService.getAnniversariesByUserId(userId);
            return ResponseHandler.success(anniversaries);
        } catch (Exception e) {
            log.error("获取纪念日列表失败: {}", e.getMessage());
            return ResponseHandler.fail("获取纪念日列表失败：" + e.getMessage());
        }
    }

    @GetMapping("/next")
    public ResponseEntity<ApiResponse<NextAnniversaryDTO>> getNextAnniversary(@RequestParam Long userId) {
        log.info("获取用户 {} 的下一个纪念日", userId);
        
        List<Anniversary> anniversaries = anniversaryService.getAnniversariesByUserId(userId);
        if (anniversaries.isEmpty()) {
            log.info("用户 {} 没有设置任何纪念日", userId);
            return ResponseHandler.success(null);
        }

        LocalDate today = LocalDate.now();
        Anniversary nextAnniversary = null;
        long minDaysUntil = Long.MAX_VALUE;

        for (Anniversary anniversary : anniversaries) {
            LocalDate anniversaryDate = anniversary.getDate();
            // 计算距离下一个纪念日的天数
            long daysUntil = ChronoUnit.DAYS.between(today, anniversaryDate);
            // 如果纪念日已经过去，计算下一年的天数
            if (daysUntil < 0) {
                anniversaryDate = anniversaryDate.plusYears(1);
                daysUntil = ChronoUnit.DAYS.between(today, anniversaryDate);
            }
            
            if (daysUntil < minDaysUntil) {
                minDaysUntil = daysUntil;
                nextAnniversary = anniversary;
            }
        }

        if (nextAnniversary == null) {
            log.info("未能找到用户 {} 的下一个纪念日", userId);
            return ResponseHandler.success(null);
        }

        NextAnniversaryDTO dto = new NextAnniversaryDTO();
        dto.setId(nextAnniversary.getId());
        dto.setName(nextAnniversary.getName());
        dto.setDate(nextAnniversary.getDate());
        dto.setDaysUntil(minDaysUntil);
        dto.setDescription(nextAnniversary.getDescription());

        log.info("找到用户 {} 的下一个纪念日: {}, 距离还有 {} 天", 
            userId, nextAnniversary.getName(), minDaysUntil);
        
        return ResponseHandler.success(dto);
    }
} 