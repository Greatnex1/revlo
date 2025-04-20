package com.nouah.revlo.dto.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductResponse(
        Long id,
        String productName,
        String description,
        String category,
        int availableQuantity,
        BigDecimal price
) {

}