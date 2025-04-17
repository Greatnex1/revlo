package com.nouah.revlo.api;

import com.nouah.revlo.dto.request.AuthenticationRequest;
import com.nouah.revlo.dto.response.AuthenticationResponse;
import com.nouah.revlo.service.interfaces.AuthUseCase;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static com.nouah.revlo.constants.UrlConstant.URL_CONSTANT;

@AllArgsConstructor
@RestController
@RequestMapping(URL_CONSTANT + "/auth")

public class AuthApi {
    private AuthUseCase authService;
    @PostMapping("/login")
    @Operation(summary = "Login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid AuthenticationRequest loginDto) {
    return ResponseEntity.ok(authService.authenticate(loginDto));
    }
}
