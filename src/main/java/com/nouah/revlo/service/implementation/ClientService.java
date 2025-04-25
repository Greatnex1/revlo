package com.nouah.revlo.service.implementation;

import com.nouah.revlo.dto.ClientDto;
import com.nouah.revlo.dto.ClientReportDto;
import com.nouah.revlo.dto.request.PageRequestData;
import com.nouah.revlo.exception.PhoneNumberException;
import com.nouah.revlo.exception.RevloException;
import com.nouah.revlo.models.entity.AppUser;
import com.nouah.revlo.models.entity.Client;
import com.nouah.revlo.models.enums.Authority;
import com.nouah.revlo.repository.AppUserRepository;
import com.nouah.revlo.repository.ClientRepository;
import com.nouah.revlo.service.interfaces.ClientUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.nouah.revlo.service.implementation.ProductService.getPageRequestData;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientService implements ClientUseCase {

    private final ClientRepository clientRepository;

    private final AppUserRepository userRepository;

    @Override
    public void addClient(Long userId, ClientDto clientDto) {

        AppUser user = userRepository.findById(userId).orElseThrow(()->
                new UsernameNotFoundException("Invalid User"));
        if (!isValidPhoneNumber(clientDto.phoneNumber())){
            throw new PhoneNumberException("Invalid phone number");
        }
        clientDto.validateClientDtoData();
        Client newClient = Client.builder()
                .firstName(clientDto.firstName())
                .lastName(clientDto.lastName())
                .email(clientDto.email())
                .address(clientDto.address())
                .phoneNumber(clientDto.phoneNumber())
                .authority(Authority.valueOf(clientDto.authority()))
                .totalSpent(clientDto.totalSpent())
                .createdBy(userId)
                .dateCreated(LocalDateTime.now())
                .build();
        log.info("{} {} was registered by {}", newClient.getFirstName(),newClient.getLastName(),user.getUsername() );
      log.info("total spent on a product: {}", newClient.getTotalSpent());
       clientRepository.save(newClient);
    }

    @Override
    public boolean updateClient(String phoneNumber, ClientDto clientDto) throws RevloException {
        Client existingClient = clientRepository.findByPhoneNumber(phoneNumber).
                orElseThrow(()-> new RevloException("Client with this phone number does not exist"));
        if (!isValidPhoneNumber(clientDto.phoneNumber())){
            throw new IllegalArgumentException("The Phone number is not valid");
        }
        existingClient.setFirstName(clientDto.firstName());
        existingClient.setLastName(clientDto.lastName());
        existingClient.setEmail(clientDto.email());
        existingClient.setPhoneNumber(clientDto.phoneNumber());
        existingClient.setAddress(clientDto.address());
        clientRepository.save(existingClient);
        log.info("{} {} details was updated successfully", existingClient.getFirstName(),existingClient.getLastName());

        return true;
    }

    @Override
    public Client findClientByPhoneNumber(String phoneNumber) throws RevloException {
        return clientRepository.findByPhoneNumber(phoneNumber).orElseThrow(()-> new RevloException
                ("Client with this phone number does not exist"));

    }

    @Override
    public List<Client> getAllClient() {
        return clientRepository.findAll();
    }

//    @Override
//    public Page<Client> searchClient(String searchText, int page, int size) throws RevloException {
////        PageRequestData pageInfo = getPageRequestData(page, size);
////        Page<Client> clients;
////        Pageable pageable = PageRequest.of(pageInfo.page(), pageInfo.size(), Sort.by(Sort.Direction.DESC, "dateCreated"));
////        if (searchText != null) {
////            clients = clientRepository.findClientsByPhoneNumber(searchText, pageable);
////        } else {
////            clients = clientRepository.findAll(pageable);
////              }
////        return clients;
//    }



    @Override
    public Page<Client> viewAllClients(Pageable pageable) throws RevloException {
        return clientRepository.findAll(pageable);
    }

    @Override
    public ClientReportDto generateClientReport() {

        ClientReportDto clientReport = new ClientReportDto();


        List<Client> existingClients = clientRepository.findAll();

        clientReport.setTotalClients(existingClients.size());

        List<ClientDto> topSpendingClients = existingClients.stream()
                .sorted(Comparator.comparing(Client::getTotalSpent).reversed())
                .map(client -> {
                    ClientDto clientDTO = ClientDto.builder()
//                            .id(client.getId())
                            .firstName(client.getFirstName())
                            .lastName(client.getLastName())
                            .totalSpent(client.getTotalSpent())
                            .build();
                    return clientDTO;
                })
                .limit(10)
                .collect(Collectors.toList());
        clientReport.setTopSpendingClients(topSpendingClients);

        Map<String, Integer> clientActivity = new HashMap<>();
        existingClients.forEach(client -> {
            String lastInteractionMonth = client.getLastPurchaseDate().getMonth().toString();
            clientActivity.put(lastInteractionMonth, clientActivity.getOrDefault(lastInteractionMonth, 0) + 1);
        });
        clientReport.setClientActivity(clientActivity);

        Map<String, Integer> locationStatistics = new HashMap<>();
        existingClients.forEach(client -> {
            locationStatistics.put(client.getAddress(), locationStatistics.getOrDefault(client.getAddress(), 0) + 1);
        });
        clientReport.setLocationStatistics(locationStatistics);
        log.info("Client Report was viewed by an ADMIN");
        return clientReport;
    }

    @Override
    public boolean removeClient(String phoneNumber) throws RevloException {
        Client client = findClientByPhoneNumber(phoneNumber);
        log.info("Client {} {}'s record removed. ", client.getFirstName(), client.getLastName());
        clientRepository.delete(client);

        return true;
    }


    private boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "^[0-9]{11}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }
}
