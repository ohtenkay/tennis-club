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

    /**
     * Returns the closest interval mark to the given local date time. Also
     * truncates the seconds and nanoseconds.
     *
     * @param localDateTime    the local date time to find the closest interval mark
     *                         for
     * @param intervalDuration the duration of the interval in minutes
     * @param boundary         the boundary of the interval
     * @return the closest interval mark as LocalDateTime
     */
    public static LocalDateTime getClosestIntervalMark(LocalDateTime localDateTime, int intervalDuration,
            IntervalBoundary boundary) {
        val minute = localDateTime.getMinute();
        val remainder = minute % intervalDuration;
        if (remainder == 0) {
            return localDateTime.withSecond(0).withNano(0);
        }

        val closestIntervalMark = minute - remainder + (boundary == IntervalBoundary.UPPER ? intervalDuration : 0);
        if (closestIntervalMark == 60) {
            return localDateTime.plusHours(1).withMinute(0).withSecond(0).withNano(0);
        }

        return localDateTime.withMinute(closestIntervalMark).withSecond(0).withNano(0);
    }

}
