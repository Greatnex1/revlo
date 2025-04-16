package com.nouah.revlo.dto;

import lombok.Builder;

import java.math.BigDecimal;

import static com.nouah.revlo.validator.InputValidator.validateInput;
@Builder
public record ClientDto(
         Long id,
         String firstName,
         String lastName,
         String email,
         String phoneNumber,
         String address,
         BigDecimal totalSpent,
         String authority
) {

    public void validateClientDtoData(){
        validateInput(firstName,"firstName");
        validateInput(lastName,"lastName");
        validateInput(email,"email");
        validateInput(address,"address");
        validateInput(phoneNumber,"phoneNumber");
        validateInput(authority,"authority");

    }

}
