package com.tui.fly.domain;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.regex.Pattern;

public class Airline implements Serializable {

    private static final Pattern CODE_FORMAT = Pattern.compile("[A-Z0-9]{2}");
    private static final InstanceCache<String, Airline> AIRPORTS;

    public static Airline airline(String iataCode) {
        if (iataCode == null) {
            return null;
        }
        return AIRPORTS.getCached(iataCode, new Airline(iataCode));
    }

    static {
        AIRPORTS = new InstanceCache<>();
    }

    private final String iataCode;

    public Airline(String code) {
        if (!CODE_FORMAT.matcher(code).matches()) {
            throw new IllegalArgumentException("Invalid IATA code for airline: " + code);
        }
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
