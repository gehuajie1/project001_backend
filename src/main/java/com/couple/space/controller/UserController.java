package com.couple.space.controller;

import com.couple.space.dto.UserDTO;
import com.couple.space.dto.ApiResponse;
import com.couple.space.entity.User;
import com.couple.space.service.UserService;
import com.couple.space.common.ResponseHandler;
import com.couple.space.security.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 用户控制器
 * 处理用户相关的HTTP请求，包括：
 * 1. 用户注册
 * 2. 用户登录
 * 提供RESTful API接口
 */
@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {
    /**
     * 用户服务
     * 用于处理用户相关的业务逻辑，如：
     * - 用户注册
     * - 用户登录
     * - 用户认证
     */
    private final UserService userService;
    
    /**
     * JWT令牌提供者
     * 用于生成和验证JWT令牌
     */
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 构造函数，注入依赖
     * @param userService 用户服务
     * @param jwtTokenProvider JWT令牌提供者
     */
    public UserController(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }
    
    /**
     * 用户注册接口
     * 流程：
     * 1. 接收用户注册信息
     * 2. 调用服务层进行注册
     * 3. 返回注册结果
     * @param user 用户注册信息
     * @return 注册结果，包含成功/失败状态和消息
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDTO>> register(@RequestBody User user) {
        log.info("收到用户注册请求: {}", user.getUsername());
        try {
            // 调用服务层进行注册
            User registeredUser = userService.register(user);
            // 转换为DTO对象
            UserDTO userDTO = convertToDTO(registeredUser);
            // 返回成功响应
            return ResponseHandler.success(userDTO);
        } catch (Exception e) {
            log.error("用户注册失败: {}", e.getMessage());
            // 返回错误响应
            return ResponseHandler.fail(e.getMessage());
        }
    }
    
    /**
     * 用户登录接口
     * 流程：
     * 1. 接收用户登录信息
     * 2. 调用服务层进行验证
     * 3. 返回登录结果
     * @param user 用户登录信息
     * @return 登录结果，包含成功/失败状态和消息
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, Object>>> login(@RequestBody User user) {
        try {
            // 调用服务层进行登录验证
            Optional<User> result = userService.login(user.getUsername(), user.getPassword());
            
            if (result.isPresent()) {
                // 登录成功
                User loggedInUser = result.get();
                
                // 创建UserDetails对象
                UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                    loggedInUser.getUsername(),
                    loggedInUser.getPassword(),
                    new ArrayList<>()
                );
                
                // 创建Authentication对象
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
                );
                
                // 生成JWT token
                String token = jwtTokenProvider.generateToken(authentication);
                
                // 构建响应数据
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("token", token);
                
                // 构建用户信息对象
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("id", loggedInUser.getId());
                userInfo.put("username", loggedInUser.getUsername());
                userInfo.put("createdAt", loggedInUser.getCreatedAt());
                userInfo.put("updatedAt", loggedInUser.getUpdatedAt());
                
                responseData.put("userInfo", userInfo);
                
                // 返回成功响应
                return ResponseHandler.success(responseData);
            } else {
                // 登录失败
                log.warn("用户登录失败: {}", user.getUsername());
                // 检查用户是否存在
                if (userService.existsByUsername(user.getUsername()) > 0) {
                    return ResponseHandler.fail("密码错误");
                } else {
                    return ResponseHandler.fail("用户不存在");
                }
            }
        } catch (Exception e) {
            log.error("登录过程中发生错误: {}", e.getMessage());
            return ResponseHandler.fail("登录失败：" + e.getMessage());
        }
    }

    /**
     * 将UserDTO转换为User实体
     * @param userDTO 用户DTO
     * @return User实体
     */
    private User convertToEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setCreatedAt(userDTO.getCreatedAt());
        user.setUpdatedAt(userDTO.getUpdatedAt());
        return user;
    }

    /**
     * 将User实体转换为UserDTO
     * @param user User实体
     * @return 用户DTO
     */
    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setUpdatedAt(user.getUpdatedAt());
        return userDTO;
    }
} 