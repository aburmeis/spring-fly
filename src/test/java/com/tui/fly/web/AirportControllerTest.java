package com.tui.fly.web;

import com.tui.fly.domain.Airport;
import com.tui.fly.domain.Country;
import com.tui.fly.service.AirportRegistry;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;

import static com.tui.fly.domain.Airport.airport;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AirportControllerTest {

    @InjectMocks
    private AirportController controller;
    @Mock
    private AirportRegistry registry;

    private MockMvc mvc;

    @Test
    public void apiIsReturned() throws Exception {
        mvc.perform(get("/rest/airport"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("/{iataCode}", notNullValue()))
            .andExpect(jsonPath("/search", notNullValue()));
    }

    @Test
    public void airportIsRetrievedByIataCode() throws Exception {
        Airport frankfurt = airport("FRA");
        frankfurt.setName("Frankfurt");
        when(registry.getAirport(anyString()))
            .thenReturn(frankfurt);

        mvc.perform(get("/rest/airport/FRA"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$iataCode", is("FRA")))
            .andExpect(jsonPath("$name", is("Frankfurt")));
    }

    @Test
    public void allAirportsAreRetrievedWithoutParams() throws Exception {
        when(registry.findAirports())
            .thenReturn(new HashSet<>(asList(airport("FRA"), airport("LHR"))));

        mvc.perform(get("/rest/airport/search"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$..iataCode", hasItems("FRA", "LHR")));
    }

    @Test
    public void airportsForCountryAreReturnedWithParam() throws Exception {
        when(registry.findAirports(any(Country.class)))
            .thenReturn(new HashSet<>(asList(airport("FRA"), airport("TXL"))));

        mvc.perform(get("/rest/airport/search").param("country", "DE"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$..iataCode", hasItems("FRA", "TXL")));
    }

    @Before
    public void createMvc() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
    }
}
