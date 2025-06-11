package com.nouah.revlo.api;

import com.nouah.revlo.dto.AppUserDto;
import com.nouah.revlo.dto.ResponseDto;
import com.nouah.revlo.service.implementation.AppUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.time.ZonedDateTime;

import static com.nouah.revlo.constants.ErrorMessages.REQUEST_PROCESSED;
import static com.nouah.revlo.constants.UrlConstant.URL_CONSTANT;



@AllArgsConstructor
@RestController
@RequestMapping(URL_CONSTANT + "/users")
@Tag(name="Revlo User Endpoint",
        description = "This class implements user's registration on the application"
)

public class UserApi {
    private AppUserService userService;

    @PostMapping(SIGNUP)
    @Operation(summary= "Create a new user")
    public ResponseEntity<ResponseDto> createAccount(@RequestBody @Valid AppUserDto userDto) {
        userService.userRegistration(userDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDto(REQUEST_PROCESSED,201,true, ZonedDateTime.now()));
    }
}
