package com.nouah.revlo.service.implementation;

import com.nouah.revlo.dto.ClientDto;
import com.nouah.revlo.models.entity.AppUser;
import com.nouah.revlo.models.entity.Client;
import com.nouah.revlo.models.enums.Authority;
import com.nouah.revlo.repository.AppUserRepository;
import com.nouah.revlo.repository.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Slf4j
class ClientServiceTest {


    @Mock
    private ClientRepository clientRepository;

    @Mock
    private AppUserRepository userRepository;

    @InjectMocks
    private ClientService clientService;

    ClientDto client;
    AppUser user;


    @BeforeEach
    void setUp() {
   user =  AppUser.builder()
           .firstName("John")
           .lastName("Doe")
           .phoneNumber("090345678")
           .username("Johnny")
           .build();

   client = ClientDto.builder()
           .firstName("Johnny")
           .lastName("Dogs")
           .email("johnny@gmail.com")
           .phoneNumber("09056567821")
           .authority(Authority.CLIENT.name())
           .address("678, London Street, Region")
           .build();
    }

    @Test
    void testThatClientCanBeOnboarded() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(clientRepository.save(any())).thenReturn(new Client());
        assertDoesNotThrow(() -> clientService.addClient(2L, client));

    }

    @Test
    void testThatClientCannotBeRegisteredWithAnInvalidPhoneNumberThrowsIllegalArgumentException() {
       ClientDto  client1 = ClientDto.builder()
               .phoneNumber("123456788990")
                         .build();
                when(userRepository.findById(2L)).thenReturn(java.util.Optional.of(user));
        assertThrows(IllegalArgumentException.class, () -> clientService.addClient(2L, client1));
    }


    @Test
    void getAllClient() {
        List<Client> clients = new ArrayList<>();
        clients.add(new Client());
        when(clientRepository.findAll()).thenReturn(clients);
        assertEquals(clients, clientService.getAllClient());
    }

    @Test
    void testThatClientDetailsCanBeUpdated() {
    }

    @Test
    void findClientByPhoneNumber() {
        try {
            when(clientRepository.findByPhoneNumber("090445678")).thenReturn(Optional.of(new Client()));
            assertNotNull(clientService.findClientByPhoneNumber("090445678"));
        }catch (Exception e) {
            log.error("PhoneNumber not found", e);
        }
    }


    @Test
    void generateClientReport() {
    }

    @Test
    void removeClient() {
    }

}