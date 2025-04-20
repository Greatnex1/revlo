package com.nouah.revlo.service.interfaces;

import com.nouah.revlo.dto.ProductDto;
import com.nouah.revlo.dto.ProductReportDto;
import com.nouah.revlo.dto.response.ProductResponse;
import com.nouah.revlo.exception.RevloException;
import com.nouah.revlo.models.entity.Product;

import java.util.List;

public interface ProductUseCase {
    ProductResponse createProduct(ProductDto productDto);
    boolean updateProduct(long productId, ProductDto productDto) throws RevloException;
    Product findProductById(long productId) throws RevloException;
    List<Product> getAllProduct();
    boolean deleteProduct(long productId) throws RevloException;
    ProductReportDto generateProductReport();
}
