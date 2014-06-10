package com.tui.fly.domain;

import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * The airport identified by the IATA 3 letter code.
 */
public class Airport implements Serializable {

    private static final Pattern CODE_FORMAT = Pattern.compile("[A-Z]{3}");

    public static Airport airport(String iataCode) {
        if (iataCode == null) {
            return null;
        }
        return new Airport(iataCode);
    }

    private final String iataCode;

    private Airport(String code) {
        if (!CODE_FORMAT.matcher(code).matches()) {
            throw new IllegalArgumentException("Invalid IATA code for airport: " + code);
        }
        iataCode = code;
    }

    public String getIataCode() {
        return iataCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Airport airport = (Airport)o;

        if (!iataCode.equals(airport.iataCode)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return iataCode.hashCode();
    }

    @Override
    public String toString() {
        return iataCode;
    }
}
