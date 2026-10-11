package tech.assessment.carRental.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tech.assessment.carRental.data.dto.CarRequest;
import tech.assessment.carRental.data.dto.ReservationRequest;
import tech.assessment.carRental.data.model.CarType;
import tech.assessment.carRental.data.model.Reservation;
import tech.assessment.carRental.service.CarRentalService;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CarRentalController.class)
public class CarRentalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CarRentalService carRentalService;

    private Reservation mockReservation;

    @BeforeEach
    void setUp() {
        LocalDateTime start = LocalDateTime.of(2027, 6, 1, 10, 0);
        mockReservation = new Reservation(UUID.randomUUID(), "SEDAN1", start, 3);
    }

    @Test
    void testAddCarSuccess() throws Exception {
        CarRequest request = new CarRequest();
        request.setCarId("SEDAN1");
        request.setCarType(CarType.SEDAN);

        mockMvc.perform(post("/car-rental/add-car")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Car added successfully"));
    }

    @Test
    void testAddCarValidationError() throws Exception {
        CarRequest request = new CarRequest();
        request.setCarId("");
        request.setCarType(null);

        mockMvc.perform(post("/car-rental/add-car")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testMakeReservationSuccess() throws Exception {
        ReservationRequest requestDto = new ReservationRequest();
        requestDto.setCarType(CarType.SEDAN);
        requestDto.setStartTime(LocalDateTime.of(2027, 6, 1, 10, 0));
        requestDto.setDays(3);

        when(carRentalService.makeReservation(eq(CarType.SEDAN), any(LocalDateTime.class), any(int.class)))
                .thenReturn(Optional.of(mockReservation));

        when(carRentalService.makeReservation(eq(CarType.SEDAN), any(LocalDateTime.class), eq(3)))
                .thenReturn(Optional.of(mockReservation));

        mockMvc.perform(post("/car-rental/rent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.reservationId").value(mockReservation.getReservationId().toString()))
                .andExpect(jsonPath("$.carId").value("SEDAN1"))
                .andExpect(jsonPath("$.days").value(3));
    }

    @Test
    void testMakeReservationConflict() throws Exception {
        ReservationRequest requestDto = new ReservationRequest();
        requestDto.setCarType(CarType.SUV);
        requestDto.setStartTime(LocalDateTime.of(2027, 6, 1, 10, 0));
        requestDto.setDays(2);

        when(carRentalService.makeReservation(eq(CarType.SUV), any(LocalDateTime.class), eq(2)))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/car-rental/rent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isConflict())
                .andExpect(content().string("No cars available"));
    }

    @Test
    void testMakeReservationValidationError() throws Exception {
        ReservationRequest requestDto = new ReservationRequest();
        requestDto.setCarType(CarType.SEDAN);
        requestDto.setStartTime(LocalDateTime.of(2020, 1, 1, 10, 0)); // Invalid: in the past
        requestDto.setDays(0);

        mockMvc.perform(post("/car-rental/rent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }
}