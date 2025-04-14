package com.nouah.revlo.repository;

import com.nouah.revlo.models.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
    AppUser findUserByUsernameIgnoreCase(String username);

}
