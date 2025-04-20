package com.couple.space.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class NextAnniversaryDTO {
    private Long id;                // 纪念日ID
    private String name;            // 纪念日名称
    private LocalDate date;         // 纪念日日期
    private Long daysUntil;         // 距离纪念日还有多少天
    private String description;     // 纪念日描述
} 