package com.tui.fly.service;

import com.tui.fly.domain.Airport;
import com.tui.fly.domain.Country;

import java.util.Set;

/**
 * Created by arne on 01.07.14.
 */
public interface AirportRegistry {
    Set<Airport> findAirports();

    Set<Airport> findAirports(Country country);

    Airport getAirport(String iataCode);
}
