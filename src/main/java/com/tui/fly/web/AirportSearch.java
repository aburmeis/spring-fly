package com.tui.fly.web;

import com.tui.fly.domain.Country;

class AirportSearch {

    private String iataCode;
    private Country country;

    public String getIataCode() {
        return iataCode;
    }

    public void setIataCode(String iataCode) {
        this.iataCode = iataCode;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + "(iataCode=" + iataCode + ",country=" + country + ")";
    }
}
