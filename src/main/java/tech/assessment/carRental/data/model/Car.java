package tech.assessment.carRental.data.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class Car {
    private final String carId;
    private final CarType carType;
}
