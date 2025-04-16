package com.nouah.revlo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TopProductDto {
    private Long productId;
    private String productName;
    private int unitsSold;
    private BigDecimal revenue;
}
