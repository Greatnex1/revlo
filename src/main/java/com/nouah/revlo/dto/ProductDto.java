package com.nouah.revlo.dto;

import com.nouah.revlo.validator.InputValidator;
import lombok.Builder;
import org.apache.commons.lang3.ObjectUtils;

import java.math.BigDecimal;

import static com.nouah.revlo.validator.InputValidator.validateInput;

@Builder
public record ProductDto(
         String productName,
        String description,
        String category,
         int availableQuantity,
         BigDecimal price
) {
    public void  validateProductDtoData (){
        validateInput(productName,"productName");
        validateInput(description,"description");
        validateInput(category,"category");

        }

        public void validPrice() {
            if (ObjectUtils.isEmpty(price)) {
                throw new IllegalArgumentException("price cannot be empty");
            }
        }
        public void validProductQuantity(){
          {
                if(ObjectUtils.isEmpty(availableQuantity)){
                    throw new IllegalArgumentException("available quantity cannot be empty");
                }
               if(availableQuantity < 0 || availableQuantity > 50){
               throw new IllegalArgumentException("available quantity cannot be negative or greater than 50");}
            }

            }

}
