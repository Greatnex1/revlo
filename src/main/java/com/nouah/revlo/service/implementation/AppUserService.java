package com.nouah.revlo.service.implementation;

import com.nouah.revlo.dto.AppUserDto;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class AppUserService implements AppUserUseCase {
    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public void userRegistration(AppUserDto staffDto) {
           staffDto.validateAppUserDto();
           if (!isValidPassword(staffDto.password())) {
               throw new IllegalArgumentException("password must have at least 8 characters, 1 upper case, 1 number, 1 special character");
           }

           Optional<AppUser> existingUser = userRepository.findByUsername(staffDto.username().toLowerCase());

           if (existingUser.isPresent()) {
               throw new UserAlreadyExistException("A staff with this username already exists!", HttpStatus.CONFLICT);
           }
              if (!staffDto.password().equals(staffDto.confirmPassword())) {
                  throw new IllegalArgumentException("Password does not match");
              }
                  AppUser newUser = AppUser.builder()
                           .firstName(staffDto.firstName())
                           .lastName(staffDto.lastName())
                           .username(staffDto.username())
                           .password(passwordEncoder.encode(staffDto.password()))
                           .authority(Authority.valueOf(staffDto.authority()))
                          .dateCreated(LocalDateTime.now())
                           .build();
                   userRepository.save(newUser);
                   log.info("User with username->{}, registered successfully", newUser.getUsername());
              }

    @Override
    public AppUser loadUser(String username) {
        return userRepository.findUserByUsernameIgnoreCase(username);
    }

    private boolean isValidPassword(String password){
//        password must have at least 8 characters, 1 upper case, 1 number, 1 special character
        return password.matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!])(?!.*\\s).{8,}$");
    }
}
