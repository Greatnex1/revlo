package com.nouah.revlo.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;


public record AuthenticationResponse(
        @JsonProperty("access_token")
        String accessToken,
        long userId

) {
    public static AuthenticationResponse of(String jwtToken, long userId) {
        return new AuthenticationResponse(jwtToken,userId);
    }
}
