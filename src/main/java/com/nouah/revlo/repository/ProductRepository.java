package com.nouah.revlo.repository;

import com.nouah.revlo.models.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
