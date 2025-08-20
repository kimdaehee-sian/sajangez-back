package com.kt.backendapp.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleResponse {
    
    private Long id;
    
    private String userId;
    
    private LocalDate saleDate;
    
    private BigDecimal amount;
    
    private String storeName;
    
    private String businessType;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
} 