package com.nouah.revlo.dto;

import java.math.BigDecimal;

public record SalesUpdateDto(
         int quantity,
         BigDecimal totalAmount
) {
}
