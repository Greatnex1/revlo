package com.nouah.revlo.config;

import com.nouah.revlo.security.CsrfCookieFilter;
import com.nouah.revlo.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;
import java.util.List;
@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authProvider;
    private final String[] NO_AUTH_ROUTES = {
            "/api/register","/api/login","/v3/api-docs",
            "/v3/api-docs/**", "/swagger-ui.html","/swagger-ui/**"
    };
//    private final String[] CSRF_ROUTES = {
//            "/api/register","/api/login","/api/patrons/",
//            "/api/patrons/{patronId}","/api/books/","/api/books/{bookId}",
//            "/api/borrow/{bookId}/patron/{patronId}","/api/return/{bookId}/patron/{patronId}"
//
//    };

//    private final String[] ADMIN_ROUTES = {
//            "/api/register","/api/login","/api/patrons/","/api/patrons/","/api/patrons/{patronId}",
//            "/api/patrons/{patronId}","/api/books/","/api/books/{bookId}","/api/patrons/{patronId}",
//            "/api/borrow/{bookId}/patron/{patronId}","/api/return/{bookId}/patron/{patronId}",
//            "/api/books/","/api/books/{bookId}","/api/books/{bookId}"
//    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
        requestHandler.setCsrfRequestAttributeName("_csrf");
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(corsCustomizer -> corsCustomizer.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(Collections.singletonList("http://localhost:9090"));
                    config.setAllowedMethods(Collections.singletonList("*"));
                    config.setAllowCredentials(true);
                    config.setAllowedHeaders(Collections.singletonList("*"));
                    config.setExposedHeaders(List.of("Authorization"));
                    config.setMaxAge(3600L);
                    return config;
                })).csrf((csrf) -> csrf.csrfTokenRequestHandler(requestHandler)
//                        .ignoringRequestMatchers(CSRF_ROUTES)
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .authenticationProvider(authProvider)
                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests((requests)->requests
                        .requestMatchers(NO_AUTH_ROUTES).permitAll()
//                        .requestMatchers(ADMIN_ROUTES).hasAuthority("ADMIN")
                        .anyRequest().authenticated());
        return  http.build();

    }
}
