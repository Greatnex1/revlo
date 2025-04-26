package com.nouah.revlo.service.implementation;

import com.nouah.revlo.dto.AppUserDto;
import com.nouah.revlo.exception.PhoneNumberException;
import com.nouah.revlo.exception.UserAlreadyExistException;
import com.nouah.revlo.models.entity.AppUser;
import com.nouah.revlo.models.enums.Authority;
import com.nouah.revlo.repository.AppUserRepository;
import com.nouah.revlo.service.interfaces.AppUserUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppUserService implements AppUserUseCase {
    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public void userRegistration(AppUserDto staffDto) {
           staffDto.validateAppUserDto();
           ensuringUserUniqueness(staffDto);
           buildNewUser(staffDto);
    }

    @Override
    public AppUser loadUser(String username) {
        return userRepository.findUserByUsernameIgnoreCase(username);
    }

    private boolean isValidPassword(String password){
//        password must have at least 8 characters, 1 upper case, 1 number, 1 special character
        return password.matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!])(?!.*\\s).{8,}$");
    }


//    private boolean isValidPhoneNumber(String phoneNumber){
//        return phoneNumber.matches("^\\+234[0-9]{10}$|\\+244[0-9]{10}$");
//    }
    private boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "^[0-9]{11}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    private void ensuringUserUniqueness(AppUserDto staffDto) {
        if (!isValidPassword(staffDto.password())) {
            throw new IllegalArgumentException("password must have at least 8 characters, 1 upper case, 1 number, 1 special character");
        }
        if (!isValidPhoneNumber(staffDto.phoneNumber())) {
            throw new PhoneNumberException("invalid phoneNumber format");
        }
        Optional<AppUser> existingUser = userRepository.findByUsername(staffDto.username().toLowerCase());

        if (existingUser.isPresent()) {
            throw new UserAlreadyExistException("A staff with this username already exists!", HttpStatus.CONFLICT);
        }
        if (!staffDto.password().equals(staffDto.confirmPassword())) {
            throw new IllegalArgumentException("Password does not match");
        }
    }

    private void buildNewUser(AppUserDto staffDto) {
        AppUser newUser = AppUser.builder()
                .firstName(staffDto.firstName())
                .lastName(staffDto.lastName())
                .username(staffDto.username())
                .password(passwordEncoder.encode(staffDto.password()))
                .authority(Authority.valueOf(staffDto.authority()))
                .dateCreated(LocalDateTime.now())
                .build();
        userRepository.save(newUser);
        log.info("User with username->{} registered successfully", newUser.getUsername());
    }
}
