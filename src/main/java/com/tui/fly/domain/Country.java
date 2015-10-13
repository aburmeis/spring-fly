package com.tui.fly.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

public final class Country implements Serializable {

    public static final Map<String,String> RESERVED_MAPPINGS;
    private static final Pattern CODE_FORMAT = Pattern.compile("[A-Z]{2}");
    private static final InstanceCache<String, Country> COUNTRIES;

    static {
        RESERVED_MAPPINGS = new HashMap<>();
        RESERVED_MAPPINGS.put("UK", "GB");
        RESERVED_MAPPINGS.put("AC", "GB");
        RESERVED_MAPPINGS.put("TA", "GB");
        RESERVED_MAPPINGS.put("EA", "ES");
        RESERVED_MAPPINGS.put("IC", "ES");
        RESERVED_MAPPINGS.put("CP", "FR");
        RESERVED_MAPPINGS.put("FX", "FR");
    }

    public static Country country(String isoCode) {
        if (isoCode == null) {
            return null;
        }
        if (!CODE_FORMAT.matcher(isoCode).matches()) {
            throw new IllegalArgumentException("Invalid ISO code for country: " + isoCode);
        }
        isoCode = RESERVED_MAPPINGS.getOrDefault(isoCode, isoCode);
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

    @JsonIgnore
    public List<String> getIsoCodes() {
        if (RESERVED_MAPPINGS.values().contains(isoCode)) {
            return Stream.concat(Stream.of(isoCode), RESERVED_MAPPINGS.entrySet().stream()
                .filter(entry -> entry.getValue().equals(isoCode))
                .map(Map.Entry::getKey))
                .collect(toList());
        }
        return singletonList(isoCode);
    }

    public String getName(Locale locale) {
        return new Locale("", isoCode).getDisplayCountry(locale);
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
