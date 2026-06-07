package com.example.demo.controller;

import com.example.demo.entity.Park;
import com.example.demo.service.ParkService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parks")
public class ParkController {

    private final ParkService parkService;

    public ParkController(ParkService parkService) {
        this.parkService = parkService;
    }

    @GetMapping
    public List<Park> getAllParks(@RequestParam(required = false) String state) {
        if (state != null) {
            return parkService.findByState(state);
        }
        return parkService.findAll();
    }

    @GetMapping("/{parkCode}")
    public Park getParkByCode(@PathVariable String parkCode) {
        return parkService.findByParkCode(parkCode);
    }

    @GetMapping("/free")
    public List<Park> getFreeparks() {
        return parkService.findFreeParks();
    }
}