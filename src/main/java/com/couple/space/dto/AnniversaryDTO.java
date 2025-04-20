package com.couple.space.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class AnniversaryDTO {
    private Long id;                // 纪念日ID
    private String name;            // 纪念日名称
    private LocalDate date;         // 纪念日日期
    private Long userId;            // 用户ID
    private String description;     // 纪念日描述
} 