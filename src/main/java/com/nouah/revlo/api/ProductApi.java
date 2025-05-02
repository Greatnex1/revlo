package com.nouah.revlo.api;

import com.nouah.revlo.dto.ProductDto;
import com.nouah.revlo.dto.ProductReportDto;
import com.nouah.revlo.dto.ResponseDto;
import com.nouah.revlo.dto.response.ApiResponse;
import com.nouah.revlo.dto.response.ProductResponse;
import com.nouah.revlo.exception.RevloException;
import com.nouah.revlo.models.entity.Client;
import com.nouah.revlo.models.entity.Product;
import com.nouah.revlo.service.implementation.ProductService;
import com.nouah.revlo.service.implementation.SalesService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;

import static com.nouah.revlo.constants.ErrorMessages.REQUEST_FAILED;
import static com.nouah.revlo.constants.ErrorMessages.REQUEST_PROCESSED;
import static com.nouah.revlo.constants.UrlConstant.URL_CONSTANT;

@AllArgsConstructor
@RestController
@RequestMapping(URL_CONSTANT + "/products")
public class ProductApi {

    private ProductService productService;


    @PostMapping("/create")
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(@Valid @RequestBody ProductDto productDto){
        ProductResponse createdProduct =   productService.createProduct(productDto);
        ApiResponse<ProductResponse> response = buildResponse();
        return new ResponseEntity<>(response.object(createdProduct), HttpStatus.CREATED);
    }

    @GetMapping("/search")
    @Cacheable(value = "products", key = "#productId")
    public ResponseEntity<ApiResponse<Product>> getAProduct(@RequestParam long productId) throws RevloException {
        Product createdProduct =   productService.findProductById(productId);
       ApiResponse<Product> response = buildSearchProduct();
        return new ResponseEntity<>(response.object(createdProduct), HttpStatus.FOUND);
    }

    @GetMapping("/reports/product")
    public ResponseEntity<ProductReportDto> getProductReport() {
        ProductReportDto productReport = productService.generateProductReport();
        return ResponseEntity.status(HttpStatus.OK).body(productReport);
    }

    @GetMapping()
    @Cacheable(value ="products",key = "'allProducts'")
    public ResponseEntity<List<Product>> getAllProduct(){
        List<Product> products = productService.getAllProduct();
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @PutMapping("/update")
    @CachePut(value="products", key = "#productId")
    public ResponseEntity<ResponseDto> updateProduct(@Valid @RequestParam long productId, @Valid @RequestBody ProductDto productDto) throws RevloException {
        boolean isUpdated = productService.updateProduct(productId, productDto);

        HttpStatus status = isUpdated ? HttpStatus.OK : HttpStatus.EXPECTATION_FAILED;
        int statusCode = isUpdated ? 200 : 417;

        ResponseDto response = new ResponseDto(REQUEST_PROCESSED, statusCode, true, ZonedDateTime.now());

        return ResponseEntity.status(status).body(response);
    }

    @GetMapping("/view-all")
    @Operation(summary = "View All Products")
    public ResponseEntity<Page<Product>> viewAllProduct(@RequestParam int page, @RequestParam int size) throws RevloException {
        Page<Product> products = productService.viewAllProducts(page,size);
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @DeleteMapping("/delete")
    @CacheEvict(value ="products",key = "#productId")
    public ResponseEntity<ResponseDto> deleteAProduct(@RequestParam long productId) throws  RevloException {
        boolean isUpdated = productService.deleteProduct(productId);

        HttpStatus status = isUpdated ? HttpStatus.OK : HttpStatus.EXPECTATION_FAILED;
        int statusCode = isUpdated ? 204 : 400;

        ResponseDto response = new ResponseDto(REQUEST_PROCESSED, statusCode, true, ZonedDateTime.now());

        return ResponseEntity.status(status).body(response);
    }


    private ApiResponse<ProductResponse> buildResponse(){
        return ApiResponse.<ProductResponse>builder()
                .isSuccessful(true)
                .message("Product created successfully")
                .statusCode(HttpStatus.CREATED.value())
                .timeStamp(ZonedDateTime.now())
                .build();
    }

    private ApiResponse<Product> buildSearchProduct(){
        return ApiResponse.<Product>builder()
                .isSuccessful(true)
                .message("Product found")
                .statusCode(HttpStatus.FOUND.value())
                .timeStamp(ZonedDateTime.now())
                .build();
    }
}

