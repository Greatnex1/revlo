package com.nouah.revlo.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuthenticationResponse {
    private String accessToken;
    private long userId;

    public static AuthenticationResponse of(String jwtToken, long userId){
        return new AuthenticationResponse(jwtToken,userId);
    }

//    @JsonProperty("access_token")
//    private String accessToken;
//    @JsonProperty("refresh_token")
//    private String refreshToken;
//    @JsonProperty("token_type")
//    private String tokenType;
}
