package com.nouah.revlo.config;

import com.nouah.revlo.security.CsrfCookieFilter;
import com.nouah.revlo.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
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
@EnableJpaAuditing
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authProvider;

    private final String[] NO_AUTH_ROUTES = {
            "/api/v1/users/register","/api/login","/v3/api-docs",
            "/v3/api-docs/**", "/swagger-ui.html","/swagger-ui/**"
    };
    private final String[] CSRF_ROUTES = {
            "/api/v1/users/register","/api/login","/api/clients/",
            "/api/clients/{clientId}","/api/sales/","/api/books/{salesId}",


    };

//    private final String[] ADMIN_ROUTES = {
//     "/api/v1/users/register","/api/login","/api/clients/",
//             "/api/clients/{clientId}","/api/sales/","/api/books/{salesId}",
//   };

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
                })).csrf(AbstractHttpConfigurer::disable);

//                      .ignoringRequestMatchers( "/api/v1/users/register")
//                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
//                .authenticationProvider(authProvider)
//                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
//                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
//                .authorizeHttpRequests((requests)->requests
//                        .requestMatchers(NO_AUTH_ROUTES).permitAll()
////                      .requestMatchers(ADMIN_ROUTES).hasAuthority("ADMIN")
//                        .anyRequest().authenticated());
        return  http.build();

    }
}
