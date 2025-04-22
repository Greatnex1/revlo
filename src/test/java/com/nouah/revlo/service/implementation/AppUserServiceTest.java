package com.nouah.revlo.service.implementation;

import com.nouah.revlo.dto.AppUserDto;
import com.nouah.revlo.exception.UserAlreadyExistException;
import com.nouah.revlo.models.entity.AppUser;
import com.nouah.revlo.repository.AppUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
@Slf4j
@ExtendWith(MockitoExtension.class)
class AppUserServiceTest {

    @Mock
    private AppUserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private AppUserService userService;

    AppUserDto userDto;


    @BeforeEach
    void setUp() {
         userDto = AppUserDto.builder()
                .firstName("Nouah")
                .lastName("Akoni")
                .username("Blessed")
                .password("Password@567")
                .confirmPassword("Password@567")
                .phoneNumber("08153579979")
                .authority("ADMIN")
                .build();
    }
@AfterEach()
void remove(){
        userRepository.deleteAll();
    }

    @Test
    void testThatUserCanRegister() {
     when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(userDto.password())).thenReturn("password is encoded");
       assertDoesNotThrow(() -> userService.userRegistration(userDto));
        assertNotNull(userDto);
        assertThat(userDto.username()).isEqualTo("Blessed");

    }

    @Test
    void testThatUserCannotRegisterWithAnInvalidPassword(){
        AppUserDto userDto = AppUserDto.builder()
                .firstName("Nouah")
                .lastName("Akoni")
                .username("Blessed")
                .password("Password56")
                .confirmPassword("Password56")
                .phoneNumber("+2348153579979")
                .authority("ADMIN")
                .build();
        assertThrows(IllegalArgumentException.class, () -> userService.userRegistration(userDto));

    }
    @Test
    void testThatUsernameIsAlreadyTaken(){
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(new AppUser()));
        assertThrows(UserAlreadyExistException.class, () -> {
            userService.userRegistration(userDto);
        });
        assertNotNull(userDto);
        assertThat(userDto.username()).isEqualTo("Blessed");
    }

    @Test
    void testThatPasswordDoesNotMatch(){
        AppUserDto userDto = AppUserDto.builder()
                .firstName("Nouah")
                .lastName("Akoni")
                .username("Blessed")
                .password("Password@56")
                .confirmPassword("Password@5699")
                .phoneNumber("+2348153579979")
                .authority("ADMIN")
                .build();

        assertThrows(IllegalArgumentException.class, () -> userService.userRegistration(userDto));

    }
    @Test
    void testThatUsernameCaseIsIgnored(){
        AppUser user = AppUser.builder()
                .username("JaMes")
                .build();
        when(userRepository.findUserByUsernameIgnoreCase(user.getUsername())).thenReturn(user);
        assertNotNull(userService.loadUser(user.getUsername()));
    }
}