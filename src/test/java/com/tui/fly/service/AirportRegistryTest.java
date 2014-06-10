package com.tui.fly.service;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.NoSuchElementException;

import static com.tui.fly.domain.Airport.airport;
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

    @Test(expected = NoSuchElementException.class)
    public void unknownAirportIsFound() {
        airports.getAirport("NIX");
    }

    @Before
    public void createRegistry() throws IOException {
        airports = new AirportRegistry();
    }
}