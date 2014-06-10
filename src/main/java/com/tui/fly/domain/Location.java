package com.tui.fly.domain;

import java.io.Serializable;

import static java.lang.Math.abs;

/**
 * A geographical location with latitude and longitude.
 * Both have to be in a valid range:
 * latitude: [-90..90]
 * longitude: [-180..180]
 */
public final class Location implements Serializable {

    private static final long serialVersionUID = 8549384097694070094L;

    private final double latitude;
    private final double longitude;

    public Location(double latitude, double longitude) {
        if (latitude < -90.0 || latitude > 90.0) {
            throw new IllegalArgumentException("Invalid latitude not in [-90..90] degrees: " + latitude);
        }
        if (longitude < -180.0 || longitude > 180.0) {
            throw new IllegalArgumentException("Invalid longitude not in [-180..180] degrees: " + longitude);
        }
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Location location = (Location)o;

        if (Double.compare(location.latitude, latitude) != 0) {
            return false;
        }
        if (Double.compare(location.longitude, longitude) != 0) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(latitude);
        result = (int)(temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int)(temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return (latitude > 0 ? "N" : "S") + abs(latitude) + ',' + (longitude > 0 ? 'E' : 'W') + abs(longitude);
    }
}
