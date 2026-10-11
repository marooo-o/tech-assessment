package tech.assessment.carRental.data;

import org.springframework.stereotype.Component;
import tech.assessment.carRental.data.model.Reservation;
import tech.assessment.carRental.util.ReservationUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class ReservationDataStorage {

    private final List<Reservation> reservations = new ArrayList<>();

    private final ReentrantLock lock = new ReentrantLock();

    public List<Reservation> findAll() {
        lock.lock();
        try {
            return new ArrayList<>(reservations);
        } finally {
            lock.unlock();
        }
    }

    public void saveIfAvailable(Reservation newReservation) {
        lock.lock();
        try {
            for (Reservation existing : reservations) {
                if (existing.getCarId().equals(newReservation.getCarId())) {
                    if (ReservationUtil.isOverlapping(existing.getStartTime(), existing.getEndTime(),
                            newReservation.getStartTime(), newReservation.getDays())) {
                        return;
                    }
                }
            }
            reservations.add(newReservation);
        } finally {
            lock.unlock();
        }
    }
}
