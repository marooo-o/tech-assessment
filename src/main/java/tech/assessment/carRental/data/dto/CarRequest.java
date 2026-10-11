package tech.assessment.carRental.data.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tech.assessment.carRental.data.model.CarType;

@Data
public class CarRequest {
    @NotBlank(message = "Car id must be specified")
    @NotNull(message = "Car id must be specified")
    private String carId;
    @NotNull(message = "Car type must be specified")
    private CarType carType;
}
