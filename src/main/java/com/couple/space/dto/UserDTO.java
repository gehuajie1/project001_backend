package com.couple.space.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户数据传输对象（DTO）
 * 用于在客户端和服务器之间传输用户数据
 * 不包含敏感信息（如密码）
 * 使用@Data注解自动生成：
 * 1. getter和setter方法
 * 2. toString方法
 * 3. equals和hashCode方法
 */
@Data
public class UserDTO {
    /**
     * 用户ID
     * 唯一标识一个用户
     * 用于：
     * 1. 用户标识
     * 2. 数据关联
     */
    private Long id;
    
    /**
     * 用户名
     * 用于：
     * 1. 用户显示
     * 2. 用户标识
     */
    private String username;
    
    /**
     * 创建时间
     * 记录用户账号的创建时间
     * 格式：yyyy-MM-dd HH:mm:ss
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     * 记录用户信息的最后更新时间
     * 格式：yyyy-MM-dd HH:mm:ss
     */
    private LocalDateTime updatedAt;
} 