package com.couple.space.entity;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 纪念日实体类
 * 对应数据库中的anniversaries表
 * 包含纪念日的基本信息
 */
@Data
public class Anniversary {
    /**
     * 纪念日ID
     * 主键，自增长
     */
    private Long id;
    
    /**
     * 用户ID
     * 外键，关联users表
     */
    private Long userId;
    
    /**
     * 纪念日名称
     * 例如：相识纪念日、恋爱纪念日、结婚纪念日等
     */
    private String name;
    
    /**
     * 纪念日日期
     * 格式：yyyy-MM-dd
     */
    private LocalDate date;
    
    /**
     * 纪念日描述
     * 可选的描述信息
     */
    private String description;
    
    /**
     * 是否提醒
     * true: 提醒
     * false: 不提醒
     */
    private Boolean remind;
    
    /**
     * 提醒时间
     * 格式：HH:mm
     * 例如：09:00
     */
    private LocalDateTime remindTime;
    
    /**
     * 创建时间
     */
    private LocalDate createdAt;
    
    /**
     * 更新时间
     */
    private LocalDate updatedAt;
    
    /**
     * 距离今天还有多少天
     */
    private Integer daysRemaining;
} 