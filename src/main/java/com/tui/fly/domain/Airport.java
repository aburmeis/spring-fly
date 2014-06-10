package com.tui.fly.domain;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * The airport identified by the IATA 3 letter code.
 */
public final class Airport implements Serializable {

    private static final long serialVersionUID = 8009557463082635594L;
    private static final Pattern CODE_FORMAT = Pattern.compile("[A-Z]{3}");
    private static final InstanceCache<String, Airport> AIRPORTS;

    public static Airport airport(String iataCode) {
        if (iataCode == null) {
            return null;
        }
        if (!CODE_FORMAT.matcher(iataCode).matches()) {
            throw new IllegalArgumentException("Invalid IATA code for airport: " + iataCode);
        }
        return AIRPORTS.getCached(iataCode, new Airport(iataCode));
    }

    static {
        AIRPORTS = new InstanceCache<>();
    }

    private final String iataCode;
    private Location location;

    private Airport(String code) {
        iataCode = code;
    }

    public String getIataCode() {
        return iataCode;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    private Object readResolve() throws ObjectStreamException {
        return airport(iataCode);
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
