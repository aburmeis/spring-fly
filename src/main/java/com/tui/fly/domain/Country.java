package com.tui.fly.domain;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.regex.Pattern;

public final class Country implements Serializable {

    private static final Pattern CODE_FORMAT = Pattern.compile("[A-Z]{2}");
    private static final InstanceCache<String, Country> COUNTRIES;

    public static Country country(String isoCode) {
        if (isoCode == null) {
            return null;
        }
        if (!CODE_FORMAT.matcher(isoCode).matches()) {
            throw new IllegalArgumentException("Invalid ISO code for country: " + isoCode);
        }
        return COUNTRIES.getCached(isoCode, new Country(isoCode));
    }

    static {
        COUNTRIES = new InstanceCache<>();
    }

    private final String isoCode;

    private Country(String isoCode) {
        this.isoCode = isoCode;
    }

    private Object readResolve() throws ObjectStreamException {
        return country(isoCode);
    }

    public String getIsoCode() {
        return isoCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Country country = (Country) o;

        if (!isoCode.equals(country.isoCode)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return isoCode.hashCode();
    }

    @Override
    public String toString() {
        return isoCode;
    }
}
