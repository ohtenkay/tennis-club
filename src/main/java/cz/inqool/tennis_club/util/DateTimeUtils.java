package cz.inqool.tennis_club.util;

import java.time.LocalDateTime;

import lombok.val;

public final class DateTimeUtils {

    public enum IntervalBoundary {
        UPPER, LOWER
    }

    private DateTimeUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static LocalDateTime getClosestIntervalMark(LocalDateTime localDateTime, int intervalDuration,
            IntervalBoundary boundary) {
        val minute = localDateTime.getMinute();
        val remainder = minute % intervalDuration;
        val closestIntervalMark = minute - remainder + (boundary == IntervalBoundary.UPPER ? intervalDuration : 0);

        return localDateTime.withMinute(closestIntervalMark);
    }
}
