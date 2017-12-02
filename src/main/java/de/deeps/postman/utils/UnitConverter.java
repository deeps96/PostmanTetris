package de.deeps.postman.utils;

import lombok.Getter;

import java.util.concurrent.TimeUnit;

public class UnitConverter {

    @Getter
    private static final long CONVERT_MS_TO_NS = 1000000L;

    private UnitConverter() {
        throw new IllegalStateException();
    }

    public static long convertMSIntoNS(long milliseconds) {
        return milliseconds * getCONVERT_MS_TO_NS();
    }

    private static long convertNSIntoMS(long nanoseconds) {
        return nanoseconds / getCONVERT_MS_TO_NS();
    }

    public static String convertNSToString(long nanoseconds) {
        return convertMSToString(convertNSIntoMS(nanoseconds));
    }

    private static String convertMSToString(long milliseconds) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(milliseconds),
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
    }
}
