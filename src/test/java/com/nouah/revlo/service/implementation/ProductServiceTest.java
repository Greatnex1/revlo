package com.nouah.revlo.service.implementation;

import com.nouah.revlo.dto.ProductDto;
import com.nouah.revlo.exception.RevloException;
import com.nouah.revlo.models.entity.Product;
import com.nouah.revlo.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Slf4j
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    ProductService productService;

    ProductDto productDto;


    @BeforeEach
    void setUp() {
        productDto = ProductDto.builder()
               .productName("Product One")
                .description("Testing at the moment")
                .price(BigDecimal.valueOf(100))
                .availableQuantity(15)
                .category("Testing")
                .build();
    }

    @Test
    void testThatProductCanBeCreated() {
        assertDoesNotThrow(() -> productService.createProduct(productDto));
    }

    @Test
    void testThatProductDetailsCanBeUpdated()  {
      try {
          long productId = 3L;
          Product product = new Product();
          product.setId(productId);
          productDto = ProductDto.builder()
                  .productName("Update Product")
                  .description("Update description")
                  .price(BigDecimal.valueOf(200))
                  .availableQuantity(20)
                  .category("Update Category")
                  .build();

          when(productRepository.findById(productId)).thenReturn(Optional.of(product));
          assertTrue(productService.updateProduct(productId, productDto));
      }
      catch(Exception e) {
          log.error("Error updating product", e);
      }

    }

    @Test
    void findProductById() throws RevloException {
        Product product = new Product();
        product.setId(3L);
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        assertNotNull(productService.findProductById(product.getId()));
    }

    @Test
    void getAllProduct() {
        List<Product> products = new ArrayList<>();
        when(productRepository.findAll()).thenReturn(products);
        assertEquals(products, productService.getAllProduct());
    }


    @Test
    void deleteProduct() throws RevloException {
            Product product = new Product();
            product.setId(1L);
            product.setProductName("Product One");
            when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
            assertTrue(productService.deleteProduct(product.getId()));

        }


}