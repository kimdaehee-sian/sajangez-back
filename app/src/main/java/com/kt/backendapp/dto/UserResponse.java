package com.kt.backendapp.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    
    private Long id;
    
    private String email;
    
    private String name;
    
    private String storeName;
    
    private String businessType;
    
    private String address;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
} 