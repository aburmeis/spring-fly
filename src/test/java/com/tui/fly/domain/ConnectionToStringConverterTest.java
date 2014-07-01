package com.tui.fly.domain;

import org.junit.Before;
import org.junit.Test;

import static com.tui.fly.domain.Airline.airline;
import static com.tui.fly.domain.Airport.airport;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ConnectionToStringConverterTest {
    private ConnectionToStringConverter converter;

    @Test(expected = IllegalArgumentException.class)
    public void noConnectionWithoutFlights() {
        converter.convert(new Connection(new Flight[0]));
    }

    @Test
    public void directConnectionIsJustTheFlight() {
        Flight flight = new Flight(airline("LH"), 100);
        flight.setFrom(airport("FRA"));
        flight.setTo(airport("LHR"));
        assertThat(converter.convert(new Connection(flight)), is("FRA LH100 LHR"));
    }

    @Test
    public void connectionWithStopIsAListOfAirportsAndFlights() {
        Flight flight1 = new Flight(airline("LH"), 100);
        flight1.setFrom(airport("FRA"));
        flight1.setTo(airport("LHR"));
        Flight flight2 = new Flight(airline("BA"), 200);
        flight2.setFrom(airport("LHR"));
        flight2.setTo(airport("JFK"));
        assertThat(converter.convert(new Connection(flight1, flight2)), is("FRA LH100 LHR BA200 JFK"));
    }

    @Before
    public void createConverter() {
        converter = new ConnectionToStringConverter();
    }
}