package tech.assessment.carRental.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.assessment.carRental.data.dto.CarRequest;
import tech.assessment.carRental.data.dto.ReservationRequest;
import tech.assessment.carRental.data.model.Car;
import tech.assessment.carRental.data.model.Reservation;
import tech.assessment.carRental.service.CarRentalService;

import java.util.Optional;

@RestController
@RequestMapping("/car-rental")
@RequiredArgsConstructor
public class CarRentalController {
    private final CarRentalService carRentalService;

    @PostMapping("/rent")
    public ResponseEntity<?> createReservation(@Valid @RequestBody ReservationRequest request) {
        Optional<Reservation> reservationOpt = carRentalService
                .makeReservation(request.getCarType(),request.getStartTime(),request.getDays());

        if (reservationOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("No cars available");
        }

        Reservation reservation = reservationOpt.get();

        return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
    }

    @PostMapping("/add-car")
    public ResponseEntity<String> createCar(@Valid @RequestBody CarRequest request) {
        carRentalService.addCar(request.getCarId(), request.getCarType());
        return ResponseEntity.status(HttpStatus.CREATED).body("Car added successfully");
    }

}
