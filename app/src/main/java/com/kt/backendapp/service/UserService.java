package com.kt.backendapp.service;

import com.kt.backendapp.dto.UserResponse;
import com.kt.backendapp.dto.UserUpdateRequest;
import com.kt.backendapp.entity.User;
import com.kt.backendapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    
    private final UserRepository userRepository;
    
    @Transactional(readOnly = true)
    public Optional<UserResponse> getUserByEmail(String email) {
        log.info("사용자 정보 조회: email={}", email);
        
        Optional<User> user = userRepository.findByEmail(email);
        
        if (user.isPresent()) {
            UserResponse userResponse = convertToResponse(user.get());
            log.info("사용자 정보 조회 성공: email={}, name={}", email, userResponse.getName());
            return Optional.of(userResponse);
        } else {
            log.warn("사용자를 찾을 수 없음: email={}", email);
            return Optional.empty();
        }
    }
    
    @Transactional
    public Optional<UserResponse> updateUser(String email, UserUpdateRequest request) {
        log.info("사용자 정보 수정: email={}, name={}, storeName={}", email, request.getName(), request.getStoreName());
        
        Optional<User> userOptional = userRepository.findByEmail(email);
        
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            
            // 사용자 정보 업데이트
            user.setName(request.getName());
            user.setStoreName(request.getStoreName());
            user.setBusinessType(request.getBusinessType());
            user.setAddress(request.getAddress());
            user.setUpdatedAt(LocalDateTime.now());
            
            User savedUser = userRepository.save(user);
            
            UserResponse userResponse = convertToResponse(savedUser);
            log.info("사용자 정보 수정 성공: email={}, name={}", email, userResponse.getName());
            
            return Optional.of(userResponse);
        } else {
            log.warn("수정할 사용자를 찾을 수 없음: email={}", email);
            return Optional.empty();
        }
    }
    
    private UserResponse convertToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .storeName(user.getStoreName())
                .businessType(user.getBusinessType())
                .address(user.getAddress())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
} 