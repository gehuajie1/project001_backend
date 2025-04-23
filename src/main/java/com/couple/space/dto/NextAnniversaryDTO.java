package com.couple.space.dto;

import lombok.Data;

@Data
public class NextAnniversaryDTO {
    private String title;           // 纪念日名称
    private Long days;              // 距离纪念日还有多少天
    private String state;           // 状态：success-成功，fail-失败
} 