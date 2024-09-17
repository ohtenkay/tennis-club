package cz.inqool.tennis_club.util;

import java.util.Objects;

public final class ExceptionUtils {

    private ExceptionUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Retrieves the root cause of an exception.
     *
     * @param throwable the exception to inspect
     * @return the root cause of the exception
     */
    public static Throwable getRootCause(Throwable throwable) {
        Objects.requireNonNull(throwable);
        Throwable rootCause = throwable;
        while (rootCause.getCause() != null) {
            rootCause = rootCause.getCause();
        }

        return rootCause;
    }

}
