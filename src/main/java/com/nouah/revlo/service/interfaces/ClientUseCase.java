package com.nouah.revlo.service.interfaces;

import com.nouah.revlo.dto.ClientDto;
import com.nouah.revlo.dto.ClientReportDto;
import com.nouah.revlo.exception.RevloException;
import com.nouah.revlo.models.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ClientUseCase {
    void addClient(Long userId, ClientDto clientDto);
    boolean updateClient(String phoneNumber, ClientDto clientDto) throws RevloException;
    Client findClientByPhoneNumber(String phoneNumber) throws RevloException;
    List<Client> getAllClient();
    Page<Client> viewAllClients(Pageable pageable) throws RevloException;
    ClientReportDto generateClientReport();
    boolean removeClient(String phoneNumber) throws RevloException;
}
