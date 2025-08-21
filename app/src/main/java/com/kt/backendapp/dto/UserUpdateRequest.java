package com.kt.backendapp.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {
    
    @NotBlank(message = "이름은 필수입니다")
    @Size(max = 255, message = "이름은 255자를 초과할 수 없습니다")
    private String name;
    
    @NotBlank(message = "매장명은 필수입니다")
    @Size(max = 255, message = "매장명은 255자를 초과할 수 없습니다")
    private String storeName;
    
    @NotBlank(message = "업종은 필수입니다")
    @Size(max = 255, message = "업종은 255자를 초과할 수 없습니다")
    private String businessType;
    
    @NotBlank(message = "주소는 필수입니다")
    private String address;
} 