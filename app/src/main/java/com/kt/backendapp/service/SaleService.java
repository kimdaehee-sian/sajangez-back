package com.kt.backendapp.service;

import com.kt.backendapp.dto.SaleRequest;
import com.kt.backendapp.dto.SaleResponse;
import com.kt.backendapp.entity.Sale;
import com.kt.backendapp.repository.SaleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SaleService {
    
    private final SaleRepository saleRepository;
    
    @Transactional
    public SaleResponse createSale(SaleRequest request) {
        log.info("매출 데이터 생성 시작: userId={}, date={}, amount={}", 
                request.getUserId(), request.getSaleDate(), request.getAmount());
        
        // 같은 날짜에 기존 매출이 있는지 확인
        Optional<Sale> existingSale = saleRepository.findByUserIdAndSaleDate(
                request.getUserId(), request.getSaleDate());
        
        Sale sale;
        if (existingSale.isPresent()) {
            // 기존 매출이 있으면 업데이트
            sale = existingSale.get();
            sale.setAmount(request.getAmount());
            sale.setStoreName(request.getStoreName());
            sale.setBusinessType(request.getBusinessType());
            log.info("기존 매출 데이터 업데이트: id={}", sale.getId());
        } else {
            // 새로운 매출 생성
            sale = Sale.builder()
                    .userId(request.getUserId())
                    .saleDate(request.getSaleDate())
                    .amount(request.getAmount())
                    .storeName(request.getStoreName())
                    .businessType(request.getBusinessType())
                    .build();
            log.info("새 매출 데이터 생성");
        }
        
        Sale savedSale = saleRepository.save(sale);
        log.info("매출 데이터 저장 완료: id={}", savedSale.getId());
        
        return convertToResponse(savedSale);
    }
    
    @Transactional(readOnly = true)
    public List<SaleResponse> getSalesByUserId(String userId) {
        log.info("사용자 매출 데이터 조회: userId={}", userId);
        
        List<Sale> sales = saleRepository.findByUserIdOrderBySaleDateDesc(userId);
        log.info("매출 데이터 조회 완료: count={}", sales.size());
        
        return sales.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<SaleResponse> getSalesByUserIdAndDateRange(String userId, LocalDate startDate, LocalDate endDate) {
        log.info("기간별 매출 데이터 조회: userId={}, startDate={}, endDate={}", 
                userId, startDate, endDate);
        
        List<Sale> sales = saleRepository.findByUserIdAndSaleDateBetweenOrderBySaleDate(
                userId, startDate, endDate);
        log.info("기간별 매출 데이터 조회 완료: count={}", sales.size());
        
        return sales.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Optional<SaleResponse> getSaleByUserIdAndDate(String userId, LocalDate date) {
        log.info("특정 날짜 매출 데이터 조회: userId={}, date={}", userId, date);
        
        return saleRepository.findByUserIdAndSaleDate(userId, date)
                .map(this::convertToResponse);
    }
    
    @Transactional(readOnly = true)
    public BigDecimal getTotalSales(String userId) {
        log.info("총 매출 조회: userId={}", userId);
        
        return saleRepository.findTotalSalesByUserId(userId)
                .orElse(BigDecimal.ZERO);
    }
    
    @Transactional(readOnly = true)
    public BigDecimal getAverageSales(String userId) {
        log.info("평균 매출 조회: userId={}", userId);
        
        return saleRepository.findAverageSalesByUserId(userId)
                .orElse(BigDecimal.ZERO);
    }
    
    @Transactional(readOnly = true)
    public Long getSalesCount(String userId) {
        log.info("매출 기록 수 조회: userId={}", userId);
        
        return saleRepository.countByUserId(userId);
    }
    
    @Transactional
    public void deleteSale(Long saleId, String userId) {
        log.info("매출 데이터 삭제: saleId={}, userId={}", saleId, userId);
        
        Optional<Sale> sale = saleRepository.findById(saleId);
        if (sale.isPresent() && sale.get().getUserId().equals(userId)) {
            saleRepository.deleteById(saleId);
            log.info("매출 데이터 삭제 완료: saleId={}", saleId);
        } else {
            log.warn("매출 데이터 삭제 실패 - 권한 없음: saleId={}, userId={}", saleId, userId);
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }
    }

    @Transactional
    public SaleResponse updateSale(Long saleId, String userId, SaleRequest request) {
        log.info("매출 데이터 수정: saleId={}, userId={}, request={}", saleId, userId, request);
        
        Optional<Sale> saleOptional = saleRepository.findById(saleId);
        if (saleOptional.isPresent() && saleOptional.get().getUserId().equals(userId)) {
            Sale sale = saleOptional.get();
            
            // 매출 데이터 업데이트
            sale.setAmount(request.getAmount());
            sale.setStoreName(request.getStoreName());
            sale.setBusinessType(request.getBusinessType());
            sale.setSaleDate(request.getSaleDate());
            
            Sale savedSale = saleRepository.save(sale);
            log.info("매출 데이터 수정 완료: saleId={}", saleId);
            
            return convertToResponse(savedSale);
        } else {
            log.warn("매출 데이터 수정 실패 - 권한 없음: saleId={}, userId={}", saleId, userId);
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }
    }
    
    private SaleResponse convertToResponse(Sale sale) {
        return SaleResponse.builder()
                .id(sale.getId())
                .userId(sale.getUserId())
                .saleDate(sale.getSaleDate())
                .amount(sale.getAmount())
                .storeName(sale.getStoreName())
                .businessType(sale.getBusinessType())
                .createdAt(sale.getCreatedAt())
                .updatedAt(sale.getUpdatedAt())
                .build();
    }
} 