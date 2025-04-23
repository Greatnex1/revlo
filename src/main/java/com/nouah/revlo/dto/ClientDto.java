package com.nouah.revlo.dto;

import com.nouah.revlo.validator.annotation.ValidPhoneNumber;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.math.BigDecimal;

import static com.nouah.revlo.validator.InputValidator.validateInput;
@Builder
public record ClientDto(
         String firstName,
         String lastName,
         String email,
         @ValidPhoneNumber
         String phoneNumber,
         String address,
         BigDecimal totalSpent,
         Long createdBy,
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
