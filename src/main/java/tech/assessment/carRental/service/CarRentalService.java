package tech.assessment.carRental.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.assessment.carRental.data.CarDataStorage;
import tech.assessment.carRental.data.ReservationDataStorage;
import tech.assessment.carRental.data.dto.CarRequest;
import tech.assessment.carRental.data.model.Car;
import tech.assessment.carRental.data.model.CarType;
import tech.assessment.carRental.data.model.Reservation;
import tech.assessment.carRental.util.ReservationUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CarRentalService {
    private final CarDataStorage cars;
    private final ReservationDataStorage reservations;

    public void addCar(String carId, CarType carType) {
        cars.save(new Car(carId, carType));
    }

    public Optional<Reservation> makeReservation(CarType carType, LocalDateTime startTime, int days) {
        List<Car> carsOfType = cars.findByType(carType);

        for (Car car : carsOfType) {
            if (isCarAvailable(car.getCarId(), startTime, days)) {
                Reservation reservation = new Reservation(UUID.randomUUID(), car.getCarId(), startTime, days);
                reservations.saveIfAvailable(reservation);
                return Optional.of(reservation);
            }
        }

        return Optional.empty();
    }

    private boolean isCarAvailable(String carId, LocalDateTime startTime, int days) {
        for (Reservation res : reservations.findAll()) {
            if (res.getCarId().equals(carId)
                    && ReservationUtil.isOverlapping(res.getStartTime(), res.getEndTime(), startTime, days)) {
                return false;
            }
        }
        return true;
    }

}
