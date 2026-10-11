package tech.assessment.carRental.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

@UtilityClass
public class ReservationUtil {

    public static boolean isOverlapping(LocalDateTime existingStart, LocalDateTime existingEnd,
                                        LocalDateTime requestedStart, int requestedDays) {
        LocalDateTime requestedEnd = requestedStart.plusDays(requestedDays);
        return existingStart.isBefore(requestedEnd) && requestedStart.isBefore(existingEnd);
    }
}
