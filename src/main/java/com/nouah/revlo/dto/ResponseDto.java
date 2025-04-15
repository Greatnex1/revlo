package com.nouah.revlo.dto;

import lombok.AllArgsConstructor;

import java.time.Instant;

public record ResponseDto(
        String message,
        int status,
        boolean isSuccessful,
        Instant timestamp

) {
}
