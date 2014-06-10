package com.tui.fly.service;

import com.tui.fly.domain.Connection;
import com.tui.fly.domain.Flight;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static com.tui.fly.domain.Airline.airline;
import static com.tui.fly.domain.Airport.airport;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class FlightCatalogTest {

    private FlightCatalog flights;

    @Test
    public void directDestinationsAreFound() {
        assertThat(flights.findDestinations(airport("FRA"), 0),
            allOf(hasItem(airport("LHR")), not(hasItem(airport("MIA")))));
    }

    @Test
    public void directConnectionsAreFound() {
        assertThat(flights.findConnections(airport("FRA"), airport("LHR"), 0),
            hasItem(new Connection(new Flight(airline("LH"), 100))));
    }

    @Test
    public void destinationsWithStopAreFound() {
        assertThat(flights.findDestinations(airport("FRA"), 1),
            hasItems(airport("LHR"), airport("MIA")));
    }

    @Test
    public void connectionsWithStopIsFound() {
        assertThat(flights.findConnections(airport("FRA"), airport("MIA"), 1),
            hasItem(new Connection(new Flight(airline("LH"), 100), new Flight(airline("BA"), 105))));
    }

    @Test
    public void noDestinationsAreEmpty() {
        assertThat(flights.findDestinations(airport("DEL"), 1).size(),
            is(0));
    }

    @Test
    public void noConnectionIsEmpty() {
        assertThat(flights.findConnections(airport("FRA"), airport("DEL"), 2).size(),
            is(0));
    }

    @Before
    public void createCatalog() throws IOException {
        flights = new FlightCatalog(new AirportRegistry());
    }

}