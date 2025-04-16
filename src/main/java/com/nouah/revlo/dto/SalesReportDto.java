package com.nouah.revlo.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SalesReportDto {
    private int totalSales;
    private BigDecimal totalRevenue;
    private List<TopProductDto> topSellingProducts;
    private List<TopSellerDto> topPerformingSellers;


}
