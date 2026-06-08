package com.example.demo.service;

import com.example.demo.dto.ParkDto;
import com.example.demo.entity.Activity;
import com.example.demo.entity.EntranceFee;
import com.example.demo.entity.Park;
import com.example.demo.entity.ParkImage;
import com.example.demo.entity.Topic;
import com.example.demo.repository.ActivityRepository;
import com.example.demo.repository.ParkRepository;
import com.example.demo.repository.TopicRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ParkService {

    private final ParkRepository parkRepository;
    private final ActivityRepository activityRepository;
    private final TopicRepository topicRepository;
    private final String PARK_NOT_FOUND_PREFIX = "Park not found: ";

    public ParkService(ParkRepository parkRepository,
                       ActivityRepository activityRepository,
                       TopicRepository topicRepository) {
        this.parkRepository = parkRepository;
        this.activityRepository = activityRepository;
        this.topicRepository = topicRepository;
    }

    @Transactional
    public void saveAll(List<ParkDto> parkDtos) {
        for (ParkDto dto : parkDtos) {
            if (parkRepository.findByParkCode(dto.getParkCode()).isPresent()) {
                continue;
            }
            Park park = mapToEntity(dto);
            parkRepository.save(park);
        }
    }

    private Park mapToEntity(ParkDto dto) {
        Park park = new Park();
        park.setId(dto.getId());
        park.setParkCode(dto.getParkCode());
        park.setFullName(dto.getFullName());
        park.setName(dto.getName());
        park.setDesignation(dto.getDesignation());
        park.setDescription(dto.getDescription());
        park.setLatitude(dto.getLatitude());
        park.setLongitude(dto.getLongitude());
        park.setStates(dto.getStates());
        park.setWeatherInfo(dto.getWeatherInfo());
        park.setDirectionsInfo(dto.getDirectionsInfo());
        park.setDirectionsUrl(dto.getDirectionsUrl());
        park.setUrl(dto.getUrl());

        if (dto.getActivities() != null) {
            Set<Activity> activities = new HashSet<>();
            for (var activityDto : dto.getActivities()) {
                Activity activity = activityRepository.findById(activityDto.getId())
                        .orElseGet(() -> {
                            Activity a = new Activity();
                            a.setId(activityDto.getId());
                            a.setName(activityDto.getName());
                            return activityRepository.save(a);
                        });
                activities.add(activity);
            }
            park.setActivities(activities);
        }

        if (dto.getTopics() != null) {
            Set<Topic> topics = new HashSet<>();
            for (var topicDto : dto.getTopics()) {
                Topic topic = topicRepository.findById(topicDto.getId())
                        .orElseGet(() -> {
                            Topic t = new Topic();
                            t.setId(topicDto.getId());
                            t.setName(topicDto.getName());
                            return topicRepository.save(t);
                        });
                topics.add(topic);
            }
            park.setTopics(topics);
        }

        if (dto.getEntranceFees() != null) {
            List<EntranceFee> fees = dto.getEntranceFees().stream()
                    .map(feeDto -> {
                        EntranceFee fee = new EntranceFee();
                        fee.setTitle(feeDto.getTitle());
                        fee.setDescription(feeDto.getDescription());
                        fee.setCost(feeDto.getCost() != null && !feeDto.getCost().isBlank()
                                ? new BigDecimal(feeDto.getCost())
                                : BigDecimal.ZERO);
                        fee.setPark(park);
                        return fee;
                    }).toList();
            park.setEntranceFees(fees);
        }

        if (dto.getImages() != null) {
            List<ParkImage> images = dto.getImages().stream()
                    .map(imageDto -> {
                        ParkImage image = new ParkImage();
                        image.setTitle(imageDto.getTitle());
                        image.setAltText(imageDto.getAltText());
                        image.setCaption(imageDto.getCaption());
                        image.setCredit(imageDto.getCredit());
                        image.setUrl(imageDto.getUrl());
                        image.setPark(park);
                        return image;
                    }).toList();
            park.setImages(images);
        }

        return park;
    }

    public List<Park> findAll() {
        return parkRepository.findAll();
    }

    public Park findByParkCode(String parkCode) {
        return parkRepository.findByParkCode(parkCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, PARK_NOT_FOUND_PREFIX + parkCode));
    }

    public List<Park> findByState(String state) {
        return parkRepository.findByStatesContaining(state);
    }

    public List<Park> findFreeParks() {
        return parkRepository.findAll().stream()
                .filter(p -> p.getEntranceFees() == null || p.getEntranceFees().isEmpty())
                .toList();
    }
}