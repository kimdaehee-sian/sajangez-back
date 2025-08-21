package com.kt.backendapp.controller;

import com.kt.backendapp.dto.UserResponse;
import com.kt.backendapp.dto.UserUpdateRequest;
import com.kt.backendapp.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
public class UserController {
    
    private final UserService userService;
    
    @GetMapping("/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        try {
            log.info("사용자 정보 조회 요청: email={}", email);
            
            Optional<UserResponse> user = userService.getUserByEmail(email);
            
            if (user.isPresent()) {
                return ResponseEntity.ok(Map.of("success", true, "data", user.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("success", false, "error", "사용자를 찾을 수 없습니다"));
            }
        } catch (Exception e) {
            log.error("사용자 정보 조회 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", "사용자 정보 조회에 실패했습니다: " + e.getMessage()));
        }
    }
    
    @PutMapping("/{email}")
    public ResponseEntity<?> updateUser(@PathVariable String email, @Valid @RequestBody UserUpdateRequest request) {
        try {
            log.info("사용자 정보 수정 요청: email={}, name={}, storeName={}", email, request.getName(), request.getStoreName());
            
            Optional<UserResponse> updatedUser = userService.updateUser(email, request);
            
            if (updatedUser.isPresent()) {
                return ResponseEntity.ok(Map.of("success", true, "data", updatedUser.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("success", false, "error", "수정할 사용자를 찾을 수 없습니다"));
            }
        } catch (Exception e) {
            log.error("사용자 정보 수정 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", "사용자 정보 수정에 실패했습니다: " + e.getMessage()));
        }
    }
} 