package com.nouah.revlo.api;

import com.nouah.revlo.dto.ResponseDto;
import com.nouah.revlo.dto.SalesDto;
import com.nouah.revlo.dto.SalesReportDto;
import com.nouah.revlo.dto.SalesUpdateDto;
import com.nouah.revlo.exception.RevloException;
import com.nouah.revlo.models.entity.Sales;
import com.nouah.revlo.service.implementation.SalesService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping(URL_CONSTANT + "/sales")
public class SalesApi {

    private SalesService salesService;

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createSales(@Valid @RequestParam Long userId, @Valid @RequestBody SalesDto salesDto) throws RevloException {
        boolean isCreated = salesService.createSales(userId,salesDto);
        if (isCreated){
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(REQUEST_PROCESSED, 200,true, Instant.now()));
        }else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(REQUEST_FAILED, 417,false, Instant.now()));
        }
    }
    @GetMapping
    public ResponseEntity<Sales> findATransaction(@Valid @RequestParam long salesId) throws RevloException {
        Sales sales = salesService.findSalesById(salesId);
        return ResponseEntity.status(HttpStatus.OK).body(sales);
    }
    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateSales(@Valid @RequestParam long salesId,
                                                   @Valid @RequestParam long userId,
                                                   @Valid @RequestBody SalesUpdateDto salesUpdateDto) throws RevloException {

        boolean isUpdated = salesService.updateSales(salesId, userId, salesUpdateDto);

        HttpStatus status = isUpdated ? HttpStatus.OK : HttpStatus.EXPECTATION_FAILED;
        int statusCode = isUpdated ? 200 : 417;

        ResponseDto response = new ResponseDto(REQUEST_PROCESSED, statusCode, true, Instant.now());

        return ResponseEntity.status(status).body(response);
    }
    @GetMapping("/all/transactions")
    public ResponseEntity<List<Sales>> getAllTransactions(){
        List<Sales> sales = salesService.getAllSales();
        return ResponseEntity.status(HttpStatus.OK).body(sales);
    }
    @GetMapping("/reports/sales")
    @Operation(summary = "Sales Report")
    public ResponseEntity<SalesReportDto> getSalesReport(@Valid @RequestParam String startDate,
                                                         @Valid @RequestParam String endDate) {
        SalesReportDto salesReport = salesService.generateSalesReport(startDate, endDate);
        return ResponseEntity.status(HttpStatus.OK).body(salesReport);
    }
}


