package com.nouah.revlo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class UserAlreadyExistException extends RuntimeException {

         private  final HttpStatus status ;


    public UserAlreadyExistException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }



}
