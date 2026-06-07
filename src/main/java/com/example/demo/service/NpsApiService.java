package com.example.demo.service;

import com.example.demo.config.NpsApiProperties;
import com.example.demo.dto.NpsResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import com.example.demo.dto.ParkDto;

@Service
public class NpsApiService {

    private final NpsApiProperties npsApiProperties;
    private final RestClient restClient;
    private static final String PARKS_ENDPOINT = "/parks?api_key={key}&limit={limit}&start={start}";

    public NpsApiService(NpsApiProperties npsApiProperties) {
        this.npsApiProperties = npsApiProperties;
        this.restClient = RestClient.builder()
                .baseUrl(npsApiProperties.getBaseUrl())
                .build();
    }

    public List<ParkDto> fetchAllParks() {
        List<ParkDto> allParks = new ArrayList<>();
        int limit = 50;
        int start = 0;
        int total = Integer.MAX_VALUE;

        while (start < total) {
            NpsResponse response = restClient.get()
                    .uri(PARKS_ENDPOINT,
                            npsApiProperties.getKey(), limit, start)
                    .retrieve()
                    .body(NpsResponse.class);

            if (response == null || response.getData() == null) break;

            allParks.addAll(response.getData());
            total = Integer.parseInt(response.getTotal());
            start += limit;
        }

        return allParks;
    }
}