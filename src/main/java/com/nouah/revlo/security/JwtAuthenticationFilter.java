package com.nouah.revlo.security;

import com.nouah.revlo.exception.APIError;
import com.nouah.revlo.service.implementation.JwtService;
import com.nouah.revlo.service.interfaces.JwtUseCase;
import io.jsonwebtoken.JwtException;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUseCase jwtService;

    private final UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(@lombok.NonNull HttpServletRequest request, @lombok.NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String jwt;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            try {
                filterChain.doFilter(request, response);
            }catch (JwtException e){
                e.printStackTrace();
                setErrorResponse(HttpStatus.BAD_REQUEST, response, e);
            }catch (RuntimeException exception){
                exception.printStackTrace();
                setErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,response,exception);
            }
            return;
        }
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            if (jwtService.isTokenValid(jwt, userDetails)){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request,response);
    }

    public void setErrorResponse(HttpStatus status, HttpServletResponse response, Throwable exception){
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        APIError apiError = new APIError(status, exception.getLocalizedMessage());
        try {
            String JsonOutput = apiError.convertToJson();
            response.getWriter().write(JsonOutput);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
