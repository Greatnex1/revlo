package com.nouah.revlo.dto.request;

import lombok.Builder;

@Builder
public record AuthenticationRequest(
        String username,
        String password
) {
}
