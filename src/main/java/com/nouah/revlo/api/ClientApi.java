package com.nouah.revlo.api;

import com.nouah.revlo.dto.ClientDto;
import com.nouah.revlo.dto.ClientReportDto;
import com.nouah.revlo.dto.ResponseDto;
import com.nouah.revlo.exception.RevloException;
import com.nouah.revlo.models.entity.Client;
import com.nouah.revlo.service.implementation.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

import static com.nouah.revlo.constants.ErrorMessages.REQUEST_FAILED;
import static com.nouah.revlo.constants.ErrorMessages.REQUEST_PROCESSED;
import static com.nouah.revlo.constants.UrlConstant.URL_CONSTANT;


@AllArgsConstructor
@RestController
@RequestMapping(URL_CONSTANT + "/clients")
public class ClientApi {

    private ClientService clientService;

    @PostMapping("/create")
    @Operation(summary="Onboard a client")
    public ResponseEntity<ResponseDto> createClient(@Valid @RequestParam Long userId, @Valid @RequestBody ClientDto clientDto)  {
        clientService.addClient(userId,clientDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(REQUEST_PROCESSED, 201,true, Instant.now()));
    }
    @GetMapping("/search")
    @Operation(summary="Get a client by phone number")
    public ResponseEntity<Client> findAClient(@RequestParam String phoneNumber) throws RevloException {
        Client client = clientService.findClientByPhoneNumber(phoneNumber);
        return ResponseEntity.status(HttpStatus.OK).body(client);
    }
    @PutMapping("/update")
    @Operation(summary="Update a client details")
    public ResponseEntity<ResponseDto> updateClient(@Valid @RequestParam String phoneNumber, @Valid @RequestBody ClientDto clientDto) throws RevloException {

        boolean isUpdated = clientService.updateClient(phoneNumber, clientDto);

        HttpStatus status = isUpdated ? HttpStatus.OK : HttpStatus.EXPECTATION_FAILED;
        int statusCode = isUpdated ? 200 : 417;

        ResponseDto response = new ResponseDto(REQUEST_PROCESSED, statusCode, true, Instant.now());

        return ResponseEntity.status(status).body(response);
    }
    @GetMapping
    @Operation(summary = "Get All Client")
    public ResponseEntity<List<Client>> getAllClient(){
        List<Client> clients = clientService.getAllClient();
        return ResponseEntity.status(HttpStatus.OK).body(clients);
    }
    @GetMapping("/reports/client")
    @Operation(summary="Get Client Report")
    public ResponseEntity<ClientReportDto> getClientReport() {
        ClientReportDto clientReport = clientService.generateClientReport();
        return ResponseEntity.status(HttpStatus.OK).body(clientReport);
    }
    @DeleteMapping("/delete")
    @Operation(summary = "Remove client")
    @ApiResponse()
    public ResponseEntity<ResponseDto> deleteClient(@RequestParam String phoneNumber) throws RevloException {
        boolean isDeleted = clientService.removeClient(phoneNumber);

        HttpStatus status = isDeleted ? HttpStatus.OK : HttpStatus.EXPECTATION_FAILED;
        int code = isDeleted ? 204 : 417;
        String message = isDeleted ? REQUEST_PROCESSED : REQUEST_FAILED;

        ResponseDto response = new ResponseDto(message, code, true, Instant.now());

        return ResponseEntity.status(status).body(response);

    }

}
