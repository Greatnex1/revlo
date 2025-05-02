package com.nouah.revlo.service.implementation;

import com.nouah.revlo.dto.*;
import com.nouah.revlo.exception.RevloException;
import com.nouah.revlo.models.entity.AppUser;
import com.nouah.revlo.models.entity.Client;
import com.nouah.revlo.models.entity.Product;
import com.nouah.revlo.models.entity.Sales;
import com.nouah.revlo.repository.AppUserRepository;
import com.nouah.revlo.repository.ClientRepository;
import com.nouah.revlo.repository.ProductRepository;
import com.nouah.revlo.repository.SalesRepository;
import com.nouah.revlo.service.interfaces.SalesUseCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class SalesService implements SalesUseCase {

    private final SalesRepository salesRepository;
    private final AppUserRepository userRepository;
    private final ProductRepository productRepository;
    private final ClientRepository clientRepository;

    @Override
    public boolean createSales(Long userId, SalesDto salesDto) throws RevloException {
        AppUser existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid User"));

        Client existingClient = clientRepository.findByPhoneNumber(salesDto.getPhoneNumber())
                .orElseThrow(() -> new UsernameNotFoundException("This Client is yet to be registered"));

        BigDecimal totalBill = BigDecimal.ZERO;
        Set<Product> products = new HashSet<>();

        for (Map.Entry<Long, Integer> entry : salesDto.getProductQuantities().entrySet()) {
            Long productId = entry.getKey();
            Integer quantity = entry.getValue();

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RevloException("Product not found with ID: " + productId));

            validateQuantity(product, quantity);

            BigDecimal productTotal = calculateProductTotal(product, quantity);
            totalBill = totalBill.add(productTotal);

            updateProductStats(product, quantity, productTotal);
            productRepository.save(product);

            products.add(product);
        }
        Sales newSales = Sales.builder()
                .seller(existingUser)
                .client(existingClient)
                .quantity(salesDto.getTotalQuantity())
                .totalAmount(totalBill)
                .products(products)
                .createdBy(userId)
                .dateCreated(LocalDateTime.now())
                .build();


        updateClientStats(existingClient, totalBill);

        salesRepository.save(newSales);
        clientRepository.save(existingClient);

        log.info("A new sale was created by staff: {}", existingUser.getUsername());

        return true;
    }


    private BigDecimal calculateProductTotal(Product product, Integer quantity) {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    private void updateProductStats(Product product, Integer quantity, BigDecimal productTotal) {
        product.setTotalUnitsSoldBy(product.getTotalUnitsSoldBy() + quantity);
        product.setTotalRevenue(product.getTotalRevenue().add(productTotal));
        product.setAvailableQuantity(product.getAvailableQuantity() - quantity);
    }

    private void validateQuantity(Product product, Integer quantity) throws RevloException {
        if (quantity <= 0 || product.getAvailableQuantity() < quantity) {
            throw new RevloException("Insufficient quantity for product with ID: " + product.getId());
        }
    }

    private void updateClientStats(Client client, BigDecimal totalBill) {
        client.setTotalSpent(client.getTotalSpent().add(totalBill));
        client.setLastPurchaseDate(LocalDateTime.now());
    }

    @Override
    public boolean updateSales(long salesId, long userId, SalesUpdateDto salesUpdateDto) throws RevloException {
        Sales savedSales = salesRepository.findById(salesId).orElseThrow(() ->
                new RevloException("This transaction does not exist"));
        savedSales.setQuantity(salesUpdateDto.quantity());
        savedSales.setTotalAmount(salesUpdateDto.totalAmount());
        salesRepository.save(savedSales);
        log.info("Sales with ID : {} was updated", salesId);

        return true;
    }

    @Override
    public Sales findSalesById(long salesId) throws RevloException {
        return salesRepository.findById(salesId).orElseThrow(() ->
                new RevloException("Transaction with the ID does not exist"));
    }

    @Override
    public List<Sales> getAllSales() {
        return salesRepository.findAll();
    }

    @Override
    public SalesReportDto generateSalesReport(String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);

            List<Sales> sales = salesRepository.findAll().stream().filter(transaction ->
                    transaction.getDateCreated().isEqual(start.atStartOfDay())).filter(transaction ->
                    transaction.getDateCreated().isEqual(end.atStartOfDay())).toList();

            int totalSales = sales.size();
            log.info("Total number of sales is : {}", totalSales);
            BigDecimal totalRevenue = calculateTotalRevenue(sales);
            List<TopProductDto> topSellingProducts = calculateTopSellingProducts(sales);
            List<TopSellerDto> topPerformingSellers = calculateTopPerformingSellers(sales);

            SalesReportDto salesReport = SalesReportDto.builder()
                    .totalSales(totalSales)
                    .totalRevenue(totalRevenue)
                    .topPerformingSellers(topPerformingSellers)
                    .topSellingProducts(topSellingProducts)
                    .build();
            log.info("Sales report was generated and viewed by an ADMIN");
            return salesReport;
        }

    private BigDecimal calculateTotalRevenue(List<Sales> sales) {
        return sales.stream()
                .map(Sales::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<TopProductDto> calculateTopSellingProducts(List<Sales> sales) {
        return sales.stream()
                .flatMap(sale -> sale.getProducts().stream())
                .collect(Collectors.groupingBy(Product::getId, Collectors.summingInt(Product::getTotalUnitsSoldBy)))
                .entrySet().stream()
                .map(entry -> {
                    Product product = productRepository.findById(entry.getKey()).orElse(null);
                    if (product != null) {
                        TopProductDto dto = new TopProductDto();
                        dto.setProductId(product.getId());
                        dto.setProductName(product.getProductName());
                        dto.setUnitsSold(entry.getValue());
                        dto.setRevenue(product.getPrice().multiply(BigDecimal.valueOf(entry.getValue())));
                        return dto;
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .sorted((p1, p2) -> p2.getUnitsSold() - p1.getUnitsSold())
                .limit(10)
                .toList();
    }

    private List<TopSellerDto> calculateTopPerformingSellers(List<Sales> sales) {
        Map<AppUser, BigDecimal> sellerRevenueMap = sales.stream()
                .collect(Collectors.groupingBy(Sales::getSeller, Collectors.reducing(BigDecimal.ZERO, Sales::getTotalAmount, BigDecimal::add)));
        return sellerRevenueMap.entrySet().stream()
                .map(entry -> {
                    AppUser seller = entry.getKey();
                    BigDecimal sellerRevenue = entry.getValue();
                    TopSellerDto dto = TopSellerDto.builder()
                            .sellerId(seller.getId())
                            .sellerName(seller.getUsername())
                            .totalRevenue(sellerRevenue)
                            .build();
                    log.info("Top seller revenue: {}", dto.getTotalRevenue());
                    return dto;
                })
                .sorted((s1, s2) -> s2.getTotalRevenue().compareTo(s1.getTotalRevenue()))
                .limit(10)
                .toList();

    }
}