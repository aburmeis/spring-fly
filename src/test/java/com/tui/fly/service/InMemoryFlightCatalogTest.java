package com.tui.fly.service;

import com.tui.fly.domain.Connection;
import com.tui.fly.domain.Flight;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.NoSuchElementException;

import static com.tui.fly.domain.Airline.airline;
import static com.tui.fly.domain.Airport.airport;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class InMemoryFlightCatalogTest {

    @InjectMocks
    private InMemoryFlightCatalog flights;
    @Mock
    private AirportRegistry airports;
    @Mock
    private Resource flightData;

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
        AirportRegistry airports = Mockito.mock(InMemoryAirportRegistry.class);
        when(airports.getAirport(anyString()))
                .thenAnswer(invocation -> {
                    String code = (String) invocation.getArguments()[0];
                    if (asList("FRA", "LHR").contains(code)) {
                        return airport(code);
                    }
                    throw new NoSuchElementException("mock");
                });
        flights = new InMemoryFlightCatalog(airports, new ByteArrayResource("LH,FRA,LHR\nAB,LHR,MIA\nU,FRA,LHR".getBytes("UTF-8")));
        flights.afterPropertiesSet();
        assertThat(flights.findDestinations(airport("FRA"), 1).size(), is(1));
    }

    @Before
    public void createCatalog() throws IOException {
        MockitoAnnotations.initMocks(this);
        when(flightData.getInputStream())
            .thenReturn(new ByteArrayInputStream("LH,FRA,LHR\nAB,LHR,MIA".getBytes("UTF-8")));
        when(airports.getAirport(anyString()))
                .thenAnswer(invocation -> airport((String) invocation.getArguments()[0]));
        flights.afterPropertiesSet();
    }

}
