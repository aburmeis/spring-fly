package com.tui.fly.domain;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * The airline identified by the IATA 2 letter code.
 */
public final class Airline implements Serializable {

    private static final long serialVersionUID = 1636253326786747618L;
    private static final Pattern CODE_FORMAT = Pattern.compile("[A-Z0-9]{2}");
    private static final InstanceCache<String, Airline> AIRPORTS;

    public static Airline airline(String iataCode) {
        if (iataCode == null) {
            return null;
        }
        if (!CODE_FORMAT.matcher(iataCode).matches()) {
            throw new IllegalArgumentException("Invalid IATA code for airline: " + iataCode);
        }
        return AIRPORTS.getCached(iataCode, new Airline(iataCode));
    }

    static {
        AIRPORTS = new InstanceCache<>();
    }

    private final String iataCode;

    private Airline(String code) {
        iataCode = code;
    }

    public String getIataCode() {
        return iataCode;
    }

    private Object readResolve() throws ObjectStreamException {
        return airline(iataCode);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Airline airline = (Airline)o;

        if (!iataCode.equals(airline.iataCode)) {
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
