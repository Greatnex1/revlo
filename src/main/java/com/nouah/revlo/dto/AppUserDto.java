package com.nouah.revlo.dto;

import com.nouah.revlo.validator.InputValidator;
import com.nouah.revlo.validator.annotation.ValidPhoneNumber;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import static com.nouah.revlo.validator.InputValidator.validateInput;

@Builder
public record AppUserDto(
        @NotBlank(message="this field is required")
         String firstName,
        @NotBlank(message="this field is required")
         String lastName,
        @NotBlank(message="this field is required")
         String username,
        @NotBlank(message="this field is required")
        String password,
        @NotBlank(message="this field is required")
         String confirmPassword,
        @ValidPhoneNumber
        String phoneNumber,
        String authority)
{

    public void validateAppUserDto(){
        validateInput(firstName,"firstName");
        validateInput(lastName,"lastName");
        validateInput(username,"username");
        validateInput(password,"password");
        validateInput(phoneNumber,"phoneNumber");
    }

}
