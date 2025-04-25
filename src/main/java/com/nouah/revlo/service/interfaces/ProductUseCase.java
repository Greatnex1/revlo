package com.nouah.revlo.service.interfaces;

import com.nouah.revlo.dto.ProductDto;
import com.nouah.revlo.dto.ProductReportDto;
import com.nouah.revlo.dto.response.ProductResponse;
import com.nouah.revlo.exception.RevloException;
import com.nouah.revlo.models.entity.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductUseCase {
    ProductResponse createProduct(ProductDto productDto);
    boolean updateProduct(long productId, ProductDto productDto);
    Product findProductById(long productId) throws RevloException;
    List<Product> getAllProduct();
    Page<Product> viewAllProducts(int page, int size);

    boolean deleteProduct(long productId) throws RevloException;
    ProductReportDto generateProductReport();
}
//
//TerminalAccessoryStatus status = TerminalAccessoryStatus.RETRIEVED;
//
//List<Sim> objects = new ArrayList<>();
//        objects.add(sim2);
//Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "dateCreated"));
//Page<Sim> mockPage = new PageImpl<>(objects, pageable, objects.size());
//
//
//when(accessoryOutputPort.findAllSimByStatus(status, pageable)).thenReturn(mockPage);
//
//Page<Sim> result = terminalAccessoryService.viewAllSim(status, 0, 10);
//assertEquals(1, result.getTotalElements());
//
//verify(accessoryOutputPort, times(1)).findAllSimByStatus(status, pageable);
//verify(accessoryOutputPort, never()).findAllSim(any(Pageable.class));
//        }
