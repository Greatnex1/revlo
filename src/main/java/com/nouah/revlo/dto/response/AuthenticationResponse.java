package com.nouah.revlo.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;


public record AuthenticationResponse(
        @JsonProperty("access_token")
        String accessToken,
        String userId

) {
    public static AuthenticationResponse pass(String jwtToken, String userId) {
        return new AuthenticationResponse(jwtToken,userId);
    }
}
