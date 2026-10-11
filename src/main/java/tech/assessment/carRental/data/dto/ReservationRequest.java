package tech.assessment.carRental.data.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tech.assessment.carRental.data.model.CarType;

import java.time.LocalDateTime;

@Data
public class ReservationRequest {
    @NotNull(message = "Car type must be specified")
    private CarType carType;

    @NotNull(message = "Start time must be provided")
    @Future(message = "Start time must be in the future")
    private LocalDateTime startTime;

    @Min(value = 1, message = "Rental duration must be at least 1 day")
    private int days;
}
