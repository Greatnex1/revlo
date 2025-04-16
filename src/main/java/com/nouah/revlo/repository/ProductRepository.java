package com.nouah.revlo.repository;

import com.nouah.revlo.models.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p.totalUnitsSoldBy FROM Product p WHERE p.id = :id")
    Integer getTotalUnitsSoldById(@Param("id") long id);

    @Query("SELECT p.totalRevenue FROM Product p WHERE p.id = :id")
    BigDecimal getTotalRevenueById(@Param("id") long id);
}
