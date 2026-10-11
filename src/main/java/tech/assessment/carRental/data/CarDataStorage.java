package tech.assessment.carRental.data;

import org.springframework.stereotype.Component;
import tech.assessment.carRental.data.model.Car;
import tech.assessment.carRental.data.model.CarType;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CarDataStorage {

    private final ConcurrentHashMap<String, Car> carStorage = new ConcurrentHashMap<>();

    public void save(Car car) {
        carStorage.put(car.getCarId(), car);
    }

    public List<Car> findByType(CarType carType) {
        return carStorage.values().stream()
                .filter(car -> car.getCarType() == carType)
                .toList();
    }
}
