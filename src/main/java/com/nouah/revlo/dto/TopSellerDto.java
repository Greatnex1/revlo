package com.nouah.revlo.dto;

import lombok.*;

import java.math.BigDecimal;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TopSellerDto {
    private Long sellerId;
    private String sellerName;
    private BigDecimal totalRevenue;
}
