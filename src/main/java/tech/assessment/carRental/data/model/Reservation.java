package tech.assessment.carRental.data.model;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Reservation {
    private final UUID reservationId;
    private final String carId;
    private final int days;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;

    public Reservation(UUID reservationId, String carId, LocalDateTime startTime, int days) {
        this.reservationId = reservationId;
        this.carId = carId;
        this.startTime = startTime;
        this.days = days;
        this.endTime = startTime.plusDays(days);
    }

}
