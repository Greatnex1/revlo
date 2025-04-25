package com.nouah.revlo.service.implementation;

import com.nouah.revlo.dto.ClientDto;
import com.nouah.revlo.dto.ClientReportDto;
import com.nouah.revlo.dto.ProductDto;
import com.nouah.revlo.exception.PhoneNumberException;
import com.nouah.revlo.exception.RevloException;
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
import org.springframework.data.annotation.Transient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        assertThrows(PhoneNumberException.class, () -> clientService.addClient(2L, client1));
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
        try {
            long clientId = 3L;
            Client existingClient = new Client();
            existingClient.setId(clientId);
            existingClient.setPhoneNumber("09056567821");
            client = ClientDto.builder()
                    . firstName("Johnny")
                    .lastName("Doggs")
                    .email("johnny@gmail.com")
                    .phoneNumber("09056567821")
                    .authority(Authority.CLIENT.name())
                    .address("6, London Street, Region")
                    .build();

            when(clientRepository.findByPhoneNumber(existingClient.getPhoneNumber())).thenReturn(Optional.of(existingClient));
            assertTrue(clientService.updateClient(existingClient.getPhoneNumber(), client));
        }
        catch(Exception e) {
            log.error("Error updating client details", e);
        }
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

//
//    @Test
//    void generateClientReport() {
//        Client client1 = Client.builder()
//                .createdBy(user.getId())
//
//                .dateCreated(LocalDateTime.now())
//                .build();
//
//        Client client2 = Client.builder()
//                .createdBy(user.getId())
//
//                .dateCreated(LocalDateTime.now())
//                .build();
//
//        List<Client> clientList = List.of(client1, client2);
//
//        when(clientRepository.findAll()).thenReturn(clientList);
//
//        ClientReportDto salesReport =   clientService.generateClientReport();
//
//        assertNotNull(salesReport);
//        verify(clientRepository, times(1)).findAll();
//    }

    @Test
    void removeClient() throws RevloException {
        Client savedClient = new Client();
        savedClient.setPhoneNumber("09036578900");
        savedClient.setFirstName("William");
        savedClient.setLastName("Doe");
        savedClient.setId(1L);
        savedClient.setEmail("wills@gmail.com");
      when(clientRepository.findByPhoneNumber(savedClient.getPhoneNumber())).thenReturn(Optional.of(savedClient));
assertNotNull(savedClient);
        assertTrue(clientService.removeClient(savedClient.getPhoneNumber()));
    }

}