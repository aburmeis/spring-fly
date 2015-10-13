package com.tui.fly.web;

import com.tui.fly.domain.Airline;
import com.tui.fly.domain.Airport;
import com.tui.fly.domain.Connection;
import com.tui.fly.domain.Flight;
import com.tui.fly.service.AirportRegistry;
import com.tui.fly.service.FlightCatalog;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.tui.fly.domain.Airport.airport;
import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FlightControllerTest {
    @InjectMocks
    private FlightController controller;
    @Mock
    private AirportRegistry airports;
    @Mock
    private FlightCatalog flights;

    private MockMvc mvc;

    @Test
    public void apiIsReturned() throws Exception {
        mvc.perform(get("/rest/flight"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("/destination/{departure}", notNullValue()))
            .andExpect(jsonPath("/connection/{departure}/{destination}", notNullValue()));
    }

    @Test
    public void destinationsAreRequestedForDirectFlightsWithoutParameter() throws Exception {
        Airport FRA = airport("FRA");
        when(airports.getAirport(anyString()))
            .thenReturn(FRA);
        when(flights.findDestinations(any(Airport.class), anyInt()))
            .thenReturn(singleton(airport("LHR")));
        mvc.perform(get("/rest/flight/destination/FRA"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$..iataCode", hasItem("LHR")));
        verify(flights).findDestinations(eq(FRA), eq(0));
    }

    @Test
    public void destinationsAreRequestedForMaxStopsFlightsWithParameter() throws Exception {
        Airport FRA = airport("FRA");
        when(airports.getAirport(anyString()))
            .thenReturn(FRA);
        when(flights.findDestinations(any(Airport.class), anyInt()))
            .thenReturn(singleton(airport("LHR")));
        mvc.perform(get("/rest/flight/destination/FRA").param("maxStops", "2"))
            .andExpect(status().isOk());
        verify(flights).findDestinations(eq(FRA), eq(2));
    }

    @Test
    public void connectionsAreRequestedForDirectFlightsWithoutParameter() throws Exception {
        Airport FRA = airport("FRA");
        Airport JFK = airport("JFK");
        when(airports.getAirport(anyString()))
            .thenReturn(FRA, JFK);
        when(flights.findConnections(any(Airport.class), any(Airport.class), anyInt()))
            .thenReturn(singletonList(new Connection(singletonList(new Flight(Airline.airline("LH"), 100)))));
        mvc.perform(get("/rest/flight/connection/FRA/JFK"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$..carrier.iataCode", hasItem("LH")))
            .andExpect(jsonPath("$..number", hasItem(100)));
        verify(flights).findConnections(eq(FRA), eq(JFK), eq(0));
    }

    @Test
    public void connectionsAreRequestedForMaxStopsFlightsWithParameter() throws Exception {
        Airport FRA = airport("FRA");
        Airport JFK = airport("JFK");
        when(airports.getAirport(anyString()))
            .thenReturn(FRA, JFK);
        when(flights.findConnections(any(Airport.class), any(Airport.class), anyInt()))
            .thenReturn(singletonList(new Connection(singletonList(new Flight(Airline.airline("LH"), 100)))));
        mvc.perform(get("/rest/flight/connection/FRA/JFK").param("maxStops", "2"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$..carrier.iataCode", hasItem("LH")))
            .andExpect(jsonPath("$..number", hasItem(100)));
        verify(flights).findConnections(eq(FRA), eq(JFK), eq(2));
    }

    @Before
    public void createMvc() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
    }
}
