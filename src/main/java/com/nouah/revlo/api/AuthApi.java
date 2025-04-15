package com.nouah.revlo.api;

import com.nouah.revlo.dto.request.AuthenticationRequest;
import com.nouah.revlo.dto.response.AuthenticationResponse;
import com.nouah.revlo.exception.RevloException;
import com.nouah.revlo.service.interfaces.AuthUseCase;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.nouah.revlo.constants.UrlConstant.AUTH_URL;

@AllArgsConstructor
@RestController
@RequestMapping(AUTH_URL)
public class AuthApi {
    private AuthUseCase authService;
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid AuthenticationRequest loginDto) {
    return ResponseEntity.ok(authService.authenticate(loginDto));
    }
}
