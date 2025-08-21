package com.kt.backendapp.controller;

import com.kt.backendapp.dto.SaleRequest;
import com.kt.backendapp.dto.SaleResponse;
import com.kt.backendapp.service.SaleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
public class SaleController {
    
    private final SaleService saleService;
    
    @PostMapping
    public ResponseEntity<?> createSale(@Valid @RequestBody SaleRequest request) {
        try {
            log.info("매출 생성 요청: {}", request);
            SaleResponse response = saleService.createSale(request);
            return ResponseEntity.ok(Map.of("success", true, "data", response));
        } catch (Exception e) {
            log.error("매출 생성 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", "매출 생성에 실패했습니다: " + e.getMessage()));
        }
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getSalesByUserId(@PathVariable String userId) {
        try {
            log.info("사용자 매출 조회 요청: userId={}", userId);
            List<SaleResponse> sales = saleService.getSalesByUserId(userId);
            return ResponseEntity.ok(Map.of("success", true, "data", sales));
        } catch (Exception e) {
            log.error("매출 조회 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", "매출 조회에 실패했습니다: " + e.getMessage()));
        }
    }
    
    @GetMapping("/user/{userId}/range")
    public ResponseEntity<?> getSalesByDateRange(
            @PathVariable String userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            log.info("기간별 매출 조회 요청: userId={}, startDate={}, endDate={}", userId, startDate, endDate);
            List<SaleResponse> sales = saleService.getSalesByUserIdAndDateRange(userId, startDate, endDate);
            return ResponseEntity.ok(Map.of("success", true, "data", sales));
        } catch (Exception e) {
            log.error("기간별 매출 조회 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", "기간별 매출 조회에 실패했습니다: " + e.getMessage()));
        }
    }
    
    @GetMapping("/user/{userId}/date/{date}")
    public ResponseEntity<?> getSaleByDate(
            @PathVariable String userId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            log.info("특정 날짜 매출 조회 요청: userId={}, date={}", userId, date);
            Optional<SaleResponse> sale = saleService.getSaleByUserIdAndDate(userId, date);
            
            if (sale.isPresent()) {
                return ResponseEntity.ok(Map.of("success", true, "data", sale.get()));
            } else {
                return ResponseEntity.ok(Map.of("success", true, "data", null, "message", "해당 날짜에 매출 데이터가 없습니다"));
            }
        } catch (Exception e) {
            log.error("특정 날짜 매출 조회 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", "매출 조회에 실패했습니다: " + e.getMessage()));
        }
    }
    
    @GetMapping("/user/{userId}/statistics")
    public ResponseEntity<?> getSalesStatistics(@PathVariable String userId) {
        try {
            log.info("매출 통계 조회 요청: userId={}", userId);
            
            BigDecimal totalSales = saleService.getTotalSales(userId);
            BigDecimal averageSales = saleService.getAverageSales(userId);
            Long salesCount = saleService.getSalesCount(userId);
            
            Map<String, Object> statistics = new HashMap<>();
            statistics.put("totalSales", totalSales);
            statistics.put("averageSales", averageSales);
            statistics.put("salesCount", salesCount);
            
            return ResponseEntity.ok(Map.of("success", true, "data", statistics));
        } catch (Exception e) {
            log.error("매출 통계 조회 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", "매출 통계 조회에 실패했습니다: " + e.getMessage()));
        }
    }
    
    @DeleteMapping("/{saleId}/user/{userId}")
    public ResponseEntity<?> deleteSale(@PathVariable Long saleId, @PathVariable String userId) {
        try {
            log.info("매출 삭제 요청: saleId={}, userId={}", saleId, userId);
            saleService.deleteSale(saleId, userId);
            return ResponseEntity.ok(Map.of("success", true, "message", "매출 데이터가 삭제되었습니다"));
        } catch (IllegalArgumentException e) {
            log.warn("매출 삭제 권한 없음", e);
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("success", false, "error", e.getMessage()));
        } catch (Exception e) {
            log.error("매출 삭제 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", "매출 삭제에 실패했습니다: " + e.getMessage()));
        }
    }

    @PutMapping("/{saleId}/user/{userId}")
    public ResponseEntity<?> updateSale(@PathVariable Long saleId, @PathVariable String userId, @Valid @RequestBody SaleRequest request) {
        try {
            log.info("매출 수정 요청: saleId={}, userId={}, request={}", saleId, userId, request);
            SaleResponse response = saleService.updateSale(saleId, userId, request);
            return ResponseEntity.ok(Map.of("success", true, "data", response));
        } catch (IllegalArgumentException e) {
            log.warn("매출 수정 권한 없음", e);
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("success", false, "error", e.getMessage()));
        } catch (Exception e) {
            log.error("매출 수정 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", "매출 수정에 실패했습니다: " + e.getMessage()));
        }
    }
} 