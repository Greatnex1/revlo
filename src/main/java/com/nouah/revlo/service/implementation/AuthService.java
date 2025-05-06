package com.nouah.revlo.service.implementation;

import com.nouah.revlo.dto.request.AuthenticationRequest;
import com.nouah.revlo.dto.response.AuthenticationResponse;
import com.nouah.revlo.models.entity.AppUser;
import com.nouah.revlo.repository.AppUserRepository;
import com.nouah.revlo.security.TrustedAppUser;
import com.nouah.revlo.service.interfaces.AuthUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService implements AuthUseCase {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AppUserRepository userRepository;

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        return userAuthentication(request);
    }


    private AuthenticationResponse userAuthentication(AuthenticationRequest request) {
        Optional<AppUser> savedUser = userRepository.findByUsername(request.username().toLowerCase());
        if (savedUser.isPresent()){

            try {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(request.username().toLowerCase(), request.password())
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (BadCredentialsException e) {
                log.info("Authentication failed for : {}", request.username());
                throw new RuntimeException(e.getLocalizedMessage());
            }
            AppUser foundUser = userRepository.findByUsername(request.username().toLowerCase()).orElseThrow(() -> new IllegalArgumentException("user not found"));
            TrustedAppUser user = new TrustedAppUser(foundUser);
            String jwtToken = jwtService.generateToken(user);
            log.info("Authentication was successful for : {}", request.username());

            return AuthenticationResponse.of(jwtToken, user.getUserId());
        }
        throw new IllegalArgumentException("User not found!!!");
    }
    }
