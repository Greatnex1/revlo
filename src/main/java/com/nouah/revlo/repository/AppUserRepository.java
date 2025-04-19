package com.nouah.revlo.repository;

import com.nouah.revlo.models.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    @Query("SELECT u FROM AppUser u WHERE LOWER(u.username) = LOWER(:username)")
    Optional<AppUser> findByUsername(String username);
    AppUser findUserByUsernameIgnoreCase(String username);

}
