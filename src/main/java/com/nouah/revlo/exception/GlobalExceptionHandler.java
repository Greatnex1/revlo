package com.nouah.revlo.exception;
import jdk.jfr.Registered;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<APIError> handleUserAlreadyExists(UserAlreadyExistException ex) {
        ex.printStackTrace();
        return ResponseEntity.status(409).body(APIError.builder()
                .status(HttpStatus.CONFLICT)
                .message("User already exists, please choose another username")
                .build());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<APIError> handleInvalidPassword(IllegalArgumentException ex) {
        ex.printStackTrace();
        return ResponseEntity.badRequest().body(APIError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message("Password is invalid")
                .build());
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIError> handleValidationErrors(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));
             ex.printStackTrace();
        return ResponseEntity.badRequest().body(APIError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(ex.getLocalizedMessage())
                .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIError> handleGeneralError(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity.internalServerError().body(APIError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message("Error came from the server")
                .build());
    }
}