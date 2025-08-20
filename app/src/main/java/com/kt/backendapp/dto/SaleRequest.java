package com.kt.backendapp.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleRequest {
    
    @NotBlank(message = "사용자 ID는 필수입니다")
    private String userId;
    
    @NotNull(message = "판매 날짜는 필수입니다")
    private LocalDate saleDate;
    
    @NotNull(message = "매출 금액은 필수입니다")
    @DecimalMin(value = "0.0", inclusive = false, message = "매출 금액은 0보다 커야 합니다")
    private BigDecimal amount;
    
    private String storeName;
    
    private String businessType;
} 