package com.example.demo.service;

import com.example.demo.entity.EntranceFee;
import com.example.demo.entity.Park;
import com.example.demo.repository.ActivityRepository;
import com.example.demo.repository.ParkRepository;
import com.example.demo.repository.TopicRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParkServiceTest {

    @Mock
    ParkRepository parkRepository;
    @Mock
    ActivityRepository activityRepository;
    @Mock
    TopicRepository topicRepository;
    @InjectMocks
    ParkService parkService;

    @Test
    void findFreeParks_returnsOnlyParksWithoutEntranceFees() {
        Park free = new Park();
        free.setParkCode("free");
        free.setEntranceFees(List.of());

        Park paid = new Park();
        paid.setParkCode("paid");
        paid.setEntranceFees(List.of(new EntranceFee()));

        when(parkRepository.findAll()).thenReturn(List.of(free, paid));

        List<Park> result = parkService.findFreeParks();

        assertThat(result).extracting(Park::getParkCode).containsExactly("free");
    }
}
