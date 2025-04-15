package com.nouah.revlo.repository;

import com.nouah.revlo.models.entity.Sales;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalesRepository extends JpaRepository<Sales, Long> {
}
