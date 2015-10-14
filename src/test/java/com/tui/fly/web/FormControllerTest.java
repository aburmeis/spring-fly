package com.tui.fly.web;

import com.tui.fly.domain.Airport;
import com.tui.fly.domain.Country;
import com.tui.fly.domain.String2CountryConverter;
import com.tui.fly.service.AirportRegistry;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.tui.fly.domain.Airport.airport;
import static com.tui.fly.domain.Country.country;
import static java.util.Collections.singleton;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class FormControllerTest {

    @InjectMocks
    private FormController controller;
    @Mock
    private AirportRegistry airports;

    private MockMvc mvc;

    @Test
    public void initialFormIsRequested() throws Exception {
        mvc.perform(get("/index.html"))
            .andExpect(status().isOk())
            .andExpect(view().name("index"))
            .andExpect(model().attribute("countries", hasItems(country("DE"), country("GB"), country("US"))))
            .andExpect(model().attribute("form", instanceOf(AirportSearch.class)))
            .andExpect(model().attributeDoesNotExist("airports"));
    }

    @Test
    public void allAirportsAreRequestedOnPost() throws Exception {
        Airport FRA = airport("FRA");
        when(airports.findAirports())
            .thenReturn(singleton(FRA));

        mvc.perform(post("/airport.html"))
            .andExpect(status().isOk())
            .andExpect(view().name("airport"))
            .andExpect(model().attributeExists("countries", "form"))
            .andExpect(model().attribute("airports", hasItem(FRA)));
    }

    @Test
    public void oneAirportIsRequestedOnPostWithIataCode() throws Exception {
        Airport FRA = airport("FRA");
        when(airports.getAirport(anyString()))
            .thenReturn(FRA);

        mvc.perform(post("/airport.html").param("iataCode", "FRA"))
            .andExpect(status().isOk())
            .andExpect(view().name("airport"))
            .andExpect(model().attributeExists("countries", "form"))
            .andExpect(model().attribute("airports", hasItem(FRA)));
    }

    @Test
    public void airportsOfCountryAreRequestedOnPostWithCountry() throws Exception {
        Airport FRA = airport("FRA");
        when(airports.findAirports(any(Country.class)))
            .thenReturn(singleton(FRA));

        mvc.perform(post("/airport.html").param("country", "DE"))
            .andExpect(status().isOk())
            .andExpect(view().name("airport"))
            .andExpect(model().attributeExists("countries", "form"))
            .andExpect(model().attribute("airports", hasItem(FRA)));
    }

    @Before
    public void createMvc() {
        MockitoAnnotations.initMocks(this);
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
        conversionService.addConverter(new String2CountryConverter());
        mvc = MockMvcBuilders.standaloneSetup(controller).setConversionService(conversionService).build();
    }
}
