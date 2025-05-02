package com.nouah.revlo.service.implementation;

import com.nouah.revlo.dto.*;
import com.nouah.revlo.dto.request.PageRequestData;
import com.nouah.revlo.dto.response.ProductResponse;
import com.nouah.revlo.exception.RevloException;
import com.nouah.revlo.models.entity.Product;
import com.nouah.revlo.repository.ClientRepository;
import com.nouah.revlo.repository.ProductRepository;
import com.nouah.revlo.service.interfaces.ProductUseCase;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProductService implements ProductUseCase {

    private final ProductRepository productRepository;

    @Override
    public ProductResponse createProduct(ProductDto productDto) {
        productDto.validateProductDtoData();
        productDto.validProductQuantity();
        productDto.validPrice();

         Product newProduct = buildNewProduct(productDto);

        return buildProductResponse(newProduct);
    }

    private Product buildNewProduct(ProductDto productDto) {
        Product newProduct = Product.builder()
                .productName(productDto.productName())
                .description(productDto.description())
                .price(productDto.price())
                .availableQuantity(productDto.availableQuantity())
                .category(productDto.category())
                .totalRevenue(BigDecimal.ZERO)
                .dateCreated(LocalDateTime.now())
                .build();
        productRepository.save(newProduct);
        log.info("Product name -> {} created successfully" , newProduct.getProductName());
//        log.info("Product added by -> {} " , clientRepository.findById());
        return newProduct;
    }

    private ProductResponse buildProductResponse(Product newProduct) {
        return ProductResponse.builder()
                .id(newProduct.getId())
                .productName(newProduct.getProductName())
                .description(newProduct.getDescription())
                .price(newProduct.getPrice())
                .availableQuantity(newProduct.getAvailableQuantity())
                .category(newProduct.getCategory())
                .build();
    }

    @Override
    public boolean updateProduct(long productId, ProductDto productDto) {
        try {
            Product existingProduct = productRepository.findById(productId).orElseThrow(() ->
                    new RevloException("Product with ID " + productId + " does not exist"));
            existingProduct.setProductName(productDto.productName());
            existingProduct.setDateCreated(LocalDateTime.now());
            existingProduct.setDescription(productDto.description());
            existingProduct.setPrice(productDto.price());
            existingProduct.setAvailableQuantity(productDto.availableQuantity());
            existingProduct.setCategory(productDto.category());
            productRepository.save(existingProduct);
            log.info("Product with ID: {} updated successfully", productId);
        }  catch(RevloException e) {
            e.getMessage();
        }
        return true;
    }

    @Cacheable(value = "product", key = "#productId")
    @Override
    public Product findProductById(long productId) throws RevloException {
        simulateSlowService();
        return productRepository.findById(productId).orElseThrow(
                ()-> new RevloException("Product with ID " + productId + " does not exist"));
    }

    private void simulateSlowService() {
        try {
            Thread.sleep(3000); // 3 seconds delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    @Override
    public Page<Product> viewAllProducts(int page, int size) {
        PageRequestData pageInfo = getPageRequestData(page, size);
        Page<Product> products;
        Pageable pageable = PageRequest.of(pageInfo.page(), pageInfo.size(), Sort.by(Sort.Direction.DESC, "dateCreated"));
        products = productRepository.findAll(pageable);
        return products;
    }

    public static @NotNull PageRequestData getPageRequestData(int page, int size) {
        if (page < 0) {
            page = 0;
        }
        if (size <= 0) {
            size = 10;
        }
        return new PageRequestData(page, size);
    }




    @Override
    public boolean deleteProduct(long productId) throws RevloException {
        Product product = findProductById(productId);
        log.info("->{} is deleted.", product.getProductName());
        productRepository.delete(product);
        return true;
    }

    @Override
    public ProductReportDto generateProductReport() {
        ProductReportDto productReport = new ProductReportDto();

        try {
            List<ProductInventoryDto> inventoryStatus = productRepository.findAll().stream()
                    .map(product -> {
                        ProductInventoryDto inventoryDTO = new ProductInventoryDto(
                                product.getId(),
                                product.getProductName(),
                                product.getAvailableQuantity());
                        return inventoryDTO;
                    })
                    .collect(Collectors.toList());
            productReport.setInventoryStatus(inventoryStatus);

            List<ProductSalesDto> salesPerformance = productRepository.findAll().stream()
                    .map(product -> {
                        ProductSalesDto salesDTO = new ProductSalesDto();
                        salesDTO.setProductId(product.getId());
                        salesDTO.setProductName(product.getProductName());

                        int totalUnitsSold = productRepository.getTotalUnitsSoldById(product.getId());
                        BigDecimal totalRevenue = productRepository.getTotalRevenueById(product.getId());

                        salesDTO.setTotalUnitsSold(totalUnitsSold);
                        salesDTO.setTotalRevenue(totalRevenue);

                        return salesDTO;
                    })
                    .collect(Collectors.toList());
            productReport.setSalesPerformance(salesPerformance);

            List<BigDecimal> prices = productRepository.findAll().stream()
                    .map(Product::getPrice)
                    .toList();
            BigDecimal averagePrice = prices.stream()
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(BigDecimal.valueOf(prices.size()), 2, RoundingMode.HALF_UP);
            BigDecimal minPrice = prices.stream()
                    .min(BigDecimal::compareTo)
                    .orElse(BigDecimal.ZERO);
            BigDecimal maxPrice = prices.stream()
                    .max(BigDecimal::compareTo)
                    .orElse(BigDecimal.ZERO);
            PriceAnalysisDto pricingAnalysis = new PriceAnalysisDto();
            pricingAnalysis.setAveragePrice(averagePrice);
            pricingAnalysis.setMinPrice(minPrice);
            pricingAnalysis.setMaxPrice(maxPrice);
            productReport.setPricingAnalysis(pricingAnalysis);

            log.info("Product Report was generated and viewed by an ADMIN");
        } catch (Exception e) {
            log.error("Error generating product report: {}", e.getMessage());
        }

        return productReport;
    }
}
