package com.nouah.revlo.repository;

import com.nouah.revlo.models.entity.Sales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface SalesRepository extends JpaRepository<Sales, Long> {
    @Query("SELECT SUM(s.quantity) FROM Sales s WHERE s.productId = :productId AND s.dateCreated >= :startDate")
    Integer getSalesForLastNDays(@Param("productId") Long productId, @Param("startDate") LocalDate startDate);
}

