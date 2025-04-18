package com.couple.space.common;

import org.springframework.http.ResponseEntity;
import com.couple.space.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 统一响应处理器
 * 用于处理所有Controller的响应
 */
@Slf4j
public class ResponseHandler {
    
    /**
     * 处理成功响应
     * @param data 响应数据
     * @return ResponseEntity
     */
    public static <T> ResponseEntity<ApiResponse<T>> success(T data) {
        return ResponseEntity.ok(ApiResponse.success(data));
    }
    
    /**
     * 处理失败响应
     * @param message 错误信息
     * @return ResponseEntity
     */
    public static <T> ResponseEntity<ApiResponse<T>> fail(String message) {
        log.warn("业务处理失败: {}", message);
        return ResponseEntity.ok(ApiResponse.fail(message));
    }
    
    /**
     * 处理异常响应
     * @param e 异常
     * @return ResponseEntity
     */
    public static <T> ResponseEntity<ApiResponse<T>> error(Exception e) {
        log.error("系统异常: {}", e.getMessage(), e);
        return ResponseEntity.ok(ApiResponse.fail("系统异常：" + e.getMessage()));
    }
} 