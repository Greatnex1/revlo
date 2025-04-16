package com.nouah.revlo.dto;

import lombok.Builder;

public record ProductInventoryDto(
         Long productId,
         String productName,
         int availableQuantity
) {
}
