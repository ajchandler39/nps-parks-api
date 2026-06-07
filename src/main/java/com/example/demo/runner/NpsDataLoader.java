package com.example.demo.runner;

import com.example.demo.dto.ParkDto;
import com.example.demo.service.NpsApiService;
import com.example.demo.service.ParkService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NpsDataLoader implements CommandLineRunner {

    private final NpsApiService npsApiService;
    private final ParkService parkService;
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(NpsDataLoader.class);
    private final String LOAD_MSG = "Fetching NPS park data...";
    private final String RETRIEVE_SUCCESS_MSG = "Fetched {} parks. Saving to database...";
    private final String SAVE_SUCCESS_MSG = "Data load complete.";

    public NpsDataLoader(NpsApiService npsApiService, ParkService parkService) {
        this.npsApiService = npsApiService;
        this.parkService = parkService;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info(LOAD_MSG);
        List<ParkDto> parks = npsApiService.fetchAllParks();
        System.out.println(RETRIEVE_SUCCESS_MSG + parks.size());
        parkService.saveAll(parks);
        logger.info(SAVE_SUCCESS_MSG);
    }
}