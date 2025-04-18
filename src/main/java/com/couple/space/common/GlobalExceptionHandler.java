package com.couple.space.common;

import com.couple.space.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import lombok.extern.slf4j.Slf4j;

/**
 * 全局异常处理器
 * 统一处理所有未捕获的异常
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * 处理所有未捕获的异常
     * @param e 异常
     * @return 统一格式的响应
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(Exception e) {
        log.error("全局异常处理: {}", e.getMessage(), e);
        return ResponseHandler.error(e);
    }
    
    /**
     * 处理业务异常
     * @param e 业务异常
     * @return 统一格式的响应
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Object>> handleBusinessException(RuntimeException e) {
        log.warn("业务异常: {}", e.getMessage());
        return ResponseHandler.fail(e.getMessage());
    }
} 