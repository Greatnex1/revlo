package com.nouah.revlo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClientReportDto {
    private int totalClients;
    private List<ClientDto> topSpendingClients;
    private Map<String, Integer> clientActivity;
    private Map<String, Integer> locationStatistics;
}

