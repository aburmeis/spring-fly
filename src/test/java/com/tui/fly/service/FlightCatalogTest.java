package com.tui.fly.service;

import com.tui.fly.domain.Airport;
import com.tui.fly.domain.Connection;
import com.tui.fly.domain.Flight;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.core.io.ByteArrayResource;

import java.io.IOException;
import java.util.NoSuchElementException;

import static com.tui.fly.domain.Airline.airline;
import static com.tui.fly.domain.Airport.airport;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class FlightCatalogTest {

    private FlightCatalog flights;

    @Test
    public void directDestinationsAreFound() throws IOException {
        assertThat(flights.findDestinations(airport("FRA"), 0),
                allOf(hasItem(airport("LHR")), not(hasItem(airport("MIA")))));
    }

    @Test
    public void directConnectionsAreFound() throws IOException {
        assertThat(flights.findConnections(airport("FRA"), airport("LHR"), 0),
                hasItem(new Connection(new Flight(airline("LH"), 100))));
    }

    @Test
    public void destinationsWithStopAreFound() throws IOException {
        assertThat(flights.findDestinations(airport("FRA"), 1),
                hasItems(airport("LHR"), airport("MIA")));
    }

    @Test
    public void connectionsWithStopIsFound() throws IOException {
        assertThat(flights.findConnections(airport("FRA"), airport("MIA"), 1),
                hasItem(new Connection(new Flight(airline("LH"), 100), new Flight(airline("AB"), 101))));
    }

    @Test
    public void noDestinationsAreEmpty() throws IOException {
        assertThat(flights.findDestinations(airport("DEL"), 1).size(),
                is(0));
    }

    @Test
    public void noConnectionIsEmpty() throws IOException {
        assertThat(flights.findConnections(airport("FRA"), airport("DEL"), 2).size(),
                is(0));
    }

    @Test
    public void invalidDataIsSkipped() throws IOException {
        AirportRegistry airports = Mockito.mock(AirportRegistry.class);
        when(airports.getAirport(anyString()))
                .thenAnswer(new Answer<Airport>() {
                    @Override
                    public Airport answer(InvocationOnMock invocation) throws Throwable {
                        String code = (String) invocation.getArguments()[0];
                        if (asList("FRA", "LHR").contains(code)) {
                            return airport(code);
                        }
                        throw new NoSuchElementException("mock");
                    }
                });
        flights = new FlightCatalog(airports, new ByteArrayResource("LH,FRA,LHR\nAB,LHR,MIA\nU,FRA,LHR".getBytes("UTF-8")));
        flights.afterPropertiesSet();
        assertThat(flights.findDestinations(airport("FRA"), 1).size(), is(1));
    }

    @Before
    public void createCatalog() throws IOException {
        AirportRegistry airports = Mockito.mock(AirportRegistry.class);
        when(airports.getAirport(anyString()))
                .thenAnswer(new Answer<Airport>() {
                    @Override
                    public Airport answer(InvocationOnMock invocation) throws Throwable {
                        return airport((String) invocation.getArguments()[0]);
                    }
                });
        flights = new FlightCatalog(airports, new ByteArrayResource("LH,FRA,LHR\nAB,LHR,MIA".getBytes("UTF-8")));
        flights.afterPropertiesSet();
    }

}