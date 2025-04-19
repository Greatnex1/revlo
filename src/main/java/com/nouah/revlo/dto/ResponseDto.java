package com.nouah.revlo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;

import java.time.Instant;
import java.time.ZonedDateTime;

public record ResponseDto(
        String message,
        int status,
        boolean isSuccessful,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        ZonedDateTime timestamp

) {
}
