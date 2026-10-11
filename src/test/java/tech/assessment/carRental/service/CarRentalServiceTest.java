package tech.assessment.carRental.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.assessment.carRental.data.CarDataStorage;
import tech.assessment.carRental.data.ReservationDataStorage;
import tech.assessment.carRental.data.model.CarType;
import tech.assessment.carRental.data.model.Reservation;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class CarRentalServiceTest {

    private CarRentalService carRentalService;

    @BeforeEach
    void setUp() {
        CarDataStorage carDataStorage = new CarDataStorage();
        ReservationDataStorage reservationDataStorage = new ReservationDataStorage();
        carRentalService = new CarRentalService(carDataStorage, reservationDataStorage);
        carRentalService.addCar("SEDAN1", CarType.SEDAN);
        carRentalService.addCar("SEDAN2", CarType.SEDAN);
        carRentalService.addCar("SUV1", CarType.SUV);
    }

    @Test
    void testSuccessfulReservation() {
        //GIVEN
        LocalDateTime startTime = LocalDateTime.of(2026, 11, 11, 10, 0);
        int days = 3;

        //WHEN
        Optional<Reservation> reservation = carRentalService.makeReservation(CarType.SEDAN, startTime, days);

        //THEN
        assertTrue(reservation.isPresent());
        assertEquals(3, reservation.get().getDays());
        assertEquals(LocalDateTime.of(2026, 11, 14, 10, 0), reservation.get().getEndTime());
    }

    @Test
    void testCarsLimit() {
        //GIVEN
        LocalDateTime start = LocalDateTime.of(2026, 11, 1, 10, 0);

        //WHEN
        Optional<Reservation> reservation1 = carRentalService.makeReservation(CarType.SUV, start, 2);
        Optional<Reservation> reservation2 = carRentalService.makeReservation(CarType.SUV, start, 2);

        //THEN
        assertTrue(reservation1.isPresent());
        assertTrue(reservation2.isEmpty());
    }

    @Test
    void testUnavailableCarType() {
        //GIVEN
        LocalDateTime start = LocalDateTime.of(2026, 11, 1, 10, 0);

        //WHEN
        Optional<Reservation> reservation = carRentalService.makeReservation(CarType.VAN, start, 1);

        //THEN
        assertTrue(reservation.isEmpty());
    }

    @Test
    void testNonOverlappingReservations() {
        //GIVEN
        LocalDateTime startTime1 = LocalDateTime.of(2026, 11, 1, 10, 0);
        LocalDateTime startTime2 = LocalDateTime.of(2026, 11, 4, 10, 0);
        int days = 3;

        //WHEN
        Optional<Reservation> reservation1 = carRentalService.makeReservation(CarType.SUV, startTime1, days);
        Optional<Reservation> reservation2 = carRentalService.makeReservation(CarType.SUV, startTime2, days);

        //THEN
        assertTrue(reservation1.isPresent());
        assertTrue(reservation2.isPresent());
    }

    @Test
    void testTimeOverlap() {
        //GIVEN
        LocalDateTime startTime1 = LocalDateTime.of(2026, 11, 1, 10, 0);
        LocalDateTime startTime2 = LocalDateTime.of(2026, 11, 3, 10, 0);
        int days = 3;

        //WHEN
        Optional<Reservation> reservation1 = carRentalService.makeReservation(CarType.SUV, startTime1, days);
        Optional<Reservation> reservation2 = carRentalService.makeReservation(CarType.SUV, startTime2, days);

        //THEN
        assertTrue(reservation1.isPresent());
        assertTrue(reservation2.isEmpty());
    }
}
