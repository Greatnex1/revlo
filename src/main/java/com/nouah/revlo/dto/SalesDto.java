package com.nouah.revlo.dto;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SalesDto {
    private String phoneNumber;
    private Map<Long, Integer> productQuantities = new HashMap<>();
    public int getTotalQuantity() {
        // Calculate and return the total quantity of products
        int totalQuantity = 0;
        for (int quantity : productQuantities.values()) {
            totalQuantity += quantity;
        }
        return totalQuantity;
    }


}
