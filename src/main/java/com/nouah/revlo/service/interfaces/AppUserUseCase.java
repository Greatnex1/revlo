package com.nouah.revlo.service.interfaces;

import com.nouah.revlo.dto.AppUserDto;
import com.nouah.revlo.models.entity.AppUser;

public interface AppUserUseCase {
    void userRegistration(AppUserDto userDto) throws IllegalArgumentException;
    AppUser loadUser(String username) throws NullPointerException;

}
