package com.couple.space.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户实体类
 * 对应数据库中的users表
 * 包含用户的基本信息和认证信息
 * 使用@Data注解自动生成：
 * 1. getter和setter方法
 * 2. toString方法
 * 3. equals和hashCode方法
 */
@Data
public class User {
    /**
     * 用户ID
     * 主键，自增长
     * 用于唯一标识一个用户
     */
    private Long id;
    
    /**
     * 用户名
     * 用于：
     * 1. 用户登录
     * 2. 用户显示
     * 3. 用户标识
     * 在数据库中设置为唯一
     */
    private String username;
    
    /**
     * 密码
     * 存储前端加密后的密码
     * 在数据库中不能为空
     */
    private String password;
    
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