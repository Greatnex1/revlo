package com.nouah.revlo.service.interfaces;

import com.nouah.revlo.dto.SalesDto;
import com.nouah.revlo.dto.SalesReportDto;
import com.nouah.revlo.dto.SalesUpdateDto;
import com.nouah.revlo.exception.RevloException;
import com.nouah.revlo.models.entity.Sales;

import java.util.List;

public interface SalesUseCase {
    boolean createSales(Long userId, SalesDto salesDto) throws RevloException;
    boolean updateSales(long SalesId, long userId, SalesUpdateDto salesUpdateDto) throws RevloException;
    Sales findSalesById(long salesId) throws RevloException;
    List<Sales> getAllSales();
    SalesReportDto generateSalesReport(String startDate, String endDate);
}

