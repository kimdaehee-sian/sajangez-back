package com.kt.backendapp.repository;

import com.kt.backendapp.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    
    List<Sale> findByUserIdOrderBySaleDateDesc(String userId);
    
    List<Sale> findByUserIdAndSaleDateBetweenOrderBySaleDate(
        String userId, 
        LocalDate startDate, 
        LocalDate endDate
    );
    
    Optional<Sale> findByUserIdAndSaleDate(String userId, LocalDate saleDate);
    
    @Query("SELECT SUM(s.amount) FROM Sale s WHERE s.userId = :userId")
    Optional<BigDecimal> findTotalSalesByUserId(@Param("userId") String userId);
    
    @Query("SELECT AVG(s.amount) FROM Sale s WHERE s.userId = :userId")
    Optional<BigDecimal> findAverageSalesByUserId(@Param("userId") String userId);
    
    @Query("SELECT s FROM Sale s WHERE s.userId = :userId AND YEAR(s.saleDate) = :year AND MONTH(s.saleDate) = :month ORDER BY s.saleDate")
    List<Sale> findByUserIdAndYearAndMonth(
        @Param("userId") String userId, 
        @Param("year") int year, 
        @Param("month") int month
    );
    
    Long countByUserId(String userId);
} 