package com.tui.fly.service;

import com.tui.fly.domain.Airport;
import org.junit.Test;
import org.springframework.core.io.ByteArrayResource;

import java.io.IOException;
import java.util.NoSuchElementException;

import static com.tui.fly.domain.Airport.airport;
import static com.tui.fly.domain.Country.country;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class AirportRegistryTest {

    @Test
    public void airportsAreRegistered() throws IOException {
        assertThat(createRegistry("FRA\nLHR\nJFK").findAirports(),
                hasItems(airport("FRA"), airport("LHR"), airport("JFK")));
    }

    @Test
    public void knownAirportIsFound() throws IOException {
        assertThat(createRegistry("FRA\nLHR\nJFK").getAirport("LHR").getIataCode(),
                is("LHR"));
    }

    @Test
    public void invalidAirportIsSkipped() throws IOException {
        assertThat(createRegistry("FRA\nDE\nLHR,Illegal,UK,3f,e2\nJFK").findAirports().size(), is(2));
    }

    @Test
    public void airportHasCountryAndLocation() throws IOException {
        Airport londonHeathrow = createRegistry("LHR,London-Heathrow,UK,51.477500,-0.461389").getAirport("LHR");
        assertThat(londonHeathrow.getCountry(), is(country("UK")));
        assertThat(londonHeathrow.getLocation().getLatitude(), is(51.4775));
        assertThat(londonHeathrow.getLocation().getLongitude(), is(-0.461389));
    }

    @Test(expected = NoSuchElementException.class)
    public void unknownAirportIsFound() throws IOException {
        createRegistry("LHR").getAirport("NIX");
    }

    private AirportRegistry createRegistry(String data) throws IOException {
        AirportRegistry registry = new AirportRegistry(new ByteArrayResource(data.getBytes("UTF-8")));
        registry.afterPropertiesSet();
        return registry;
    }
}