package com.tui.fly.service;

import com.tui.fly.domain.Airport;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.NoSuchElementException;

import static com.tui.fly.domain.Airport.airport;
import static com.tui.fly.domain.Country.country;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class AirportRegistryTest {

    private AirportRegistry airports;

    @Test
    public void airportsAreRegistered() {
        assertThat(airports.findAirports(),
            hasItems(airport("FRA"), airport("LHR"), airport("JFK")));
    }

    @Test
    public void knownAirportIsFound() {
        assertThat(airports.getAirport("LHR").getIataCode(),
            is("LHR"));
    }

    @Test
    public void airportHasCountryAndLocation() {
        Airport londonHeathrow = airports.getAirport("LHR");
        assertThat(londonHeathrow.getCountry(), is(country("UK")));
        assertThat(londonHeathrow.getLocation().getLatitude(), is(51.4775));
        assertThat(londonHeathrow.getLocation().getLongitude(), is(-0.461389));
    }

    @Test(expected = NoSuchElementException.class)
    public void unknownAirportIsFound() {
        airports.getAirport("NIX");
    }

    @Before
    public void createRegistry() throws IOException {
        airports = new AirportRegistry();
    }
}