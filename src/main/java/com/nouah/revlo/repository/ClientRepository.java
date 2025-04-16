package com.nouah.revlo.repository;

import com.nouah.revlo.models.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByPhoneNumber(String phoneNumber);
}
