package cz.inqool.tennis_club.util;

import java.util.Optional;

public final class PhoneNumberUtils {

    private PhoneNumberUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Normalizes the phone number to the Czech international format +420XXXXXXXXX.
     *
     * @param phoneNumber the phone number to normalize
     * @return the normalized phone number
     */
    public static String normalizePhoneNumber(String phoneNumber) {
        return Optional.ofNullable(phoneNumber)
                .map(pn -> pn.replaceAll("\\s+", ""))
                .map(pn -> pn.startsWith("00") ? pn.replaceFirst("00", "+") : pn)
                .map(pn -> pn.startsWith("420") ? "+" + pn : pn)
                .map(pn -> pn.startsWith("+420") ? pn : "+420" + pn)
                .orElse(phoneNumber);
    }

}
