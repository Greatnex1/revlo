package com.nouah.revlo.service.implementation;

import com.nouah.revlo.dto.ClientDto;
import com.nouah.revlo.dto.SalesDto;
import com.nouah.revlo.dto.SalesReportDto;
import com.nouah.revlo.dto.SalesUpdateDto;
import com.nouah.revlo.exception.RevloException;
import com.nouah.revlo.models.entity.AppUser;
import com.nouah.revlo.models.entity.Client;
import com.nouah.revlo.models.entity.Product;
import com.nouah.revlo.models.entity.Sales;
import com.nouah.revlo.models.enums.Authority;
import com.nouah.revlo.repository.AppUserRepository;
import com.nouah.revlo.repository.ClientRepository;
import com.nouah.revlo.repository.ProductRepository;
import com.nouah.revlo.repository.SalesRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
class SalesServiceTest {

    @Mock
    private SalesRepository salesRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private AppUserRepository userRepository;

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private SalesService salesService;
    SalesDto sales;
    AppUser user;
    Client client;
    Product product;
    SalesUpdateDto salesUpdateDto;

    @BeforeEach
    void setUp() {
        user =  AppUser.builder()
                .id(2L)
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("09056567811")
                .username("Johnny")
                .build();

        client = Client.builder()
                .id(2L)
                .firstName("Johnny")
                .lastName("Dogs")
                .email("johnny@gmail.com")
                .phoneNumber("09056567811")
                .totalSpent(BigDecimal.ZERO)
                .address("678, London Street, Region")
                .build();

        product = Product.builder()
                .id(2L)
                .price(BigDecimal.valueOf(100))
                .availableQuantity(10)
                .totalRevenue(BigDecimal.ZERO)
                .totalUnitsSoldBy(0)
                .build();

        sales =  SalesDto.builder()
                .phoneNumber("09056567811")
                .productQuantities(Map.of(2L, 1))
                .build();

    }

    @Test
    void testThatSalesCanBeCreated() throws RevloException {
      when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(clientRepository.findByPhoneNumber("09056567811")).thenReturn(Optional.of(client));
        when(productRepository.findById(2L)).thenReturn(Optional.of(product));

        assertNotNull(sales);
        assertThat(sales.getTotalQuantity()).isEqualTo(1);
        assertTrue(salesService.createSales(user.getId(), sales));
    }

    @Test
    void testThatSalesCanBeUpdated() throws RevloException {
        long salesId = 1L;
        SalesUpdateDto salesUpdateDto = new SalesUpdateDto(2,BigDecimal.valueOf(200));
        Sales sales = new Sales();
        sales.setId(salesId);
        when(salesRepository.findById(salesId)).thenReturn(java.util.Optional.of(sales));
        assertTrue(salesService.updateSales(salesId, 1L, salesUpdateDto));
    }


    @Test
    void findSalesById() throws RevloException {
        long salesId = 1L;
        Sales sales = new Sales();
        sales.setId(salesId);
        when(salesRepository.findById(salesId)).thenReturn(Optional.of(sales));
        assertNotNull(salesService.findSalesById(salesId));
    }

    @Test
    void getAllSales() {
        when(salesRepository.findAll()).thenReturn(List.of(new Sales()));
        List<Sales> result = salesService.getAllSales();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(new Sales(), result.get(0));
        verify(salesRepository, times(1)).findAll();


    }

        @Test
    void generateSalesReport() {
            Sales sales1 = Sales.builder()
                    .totalAmount(BigDecimal.valueOf(100.0))
                    .quantity(2)
                    .dateCreated(LocalDateTime.now())
                    .build();

            Sales sales2 = Sales.builder()
                    .totalAmount(BigDecimal.valueOf(200.0))
                    .quantity(1)
                    .dateCreated(LocalDateTime.now().minusDays(1))
                    .build();

            List<Sales> salesList = List.of(sales1, sales2);

            when(salesRepository.findAll()).thenReturn(salesList);

         SalesReportDto salesReport =   salesService.generateSalesReport("2025-04-25","2025-05-26");

         assertNotNull(salesReport);
          verify(salesRepository, times(1)).findAll();
        }
}