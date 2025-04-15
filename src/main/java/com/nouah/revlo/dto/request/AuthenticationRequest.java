package com.nouah.revlo.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AuthenticationRequest(
        @NotBlank(message="this field is required")
        String username,
        @NotBlank(message="this field is required")
        String password
) {
}
