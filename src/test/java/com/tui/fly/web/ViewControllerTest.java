package com.tui.fly.web;

import com.tui.fly.domain.Airport;
import com.tui.fly.domain.Country;
import com.tui.fly.domain.Location;
import com.tui.fly.domain.String2CountryConverter;
import com.tui.fly.service.AirportRegistry;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static com.tui.fly.domain.Airport.airport;
import static com.tui.fly.domain.Country.country;
import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class ViewControllerTest {

    @InjectMocks
    private ViewController controller;
    @Mock
    private AirportRegistry airports;
    @Mock
    private ObjectMapper json;

    private MockMvc mvc;

    @Test
    public void initialFormIsRequested() throws Exception {
        mvc.perform(get("/index.html"))
            .andExpect(status().isOk())
            .andExpect(view().name("index"))
            .andExpect(model().attribute("countries", hasItems(country("DE"), country("GB"), country("US"))))
            .andExpect(model().attribute("form", instanceOf(AirportSearch.class)))
            .andExpect(model().attributeDoesNotExist("airports", "googleMapsApiKey", "mapData"));
    }

    @Test
    public void allAirportsAreRequestedOnPost() throws Exception {
        Airport FRA = airport("FRA");
        when(airports.findAirports())
            .thenReturn(singleton(FRA));

        mvc.perform(post("/airport.html"))
            .andExpect(status().isOk())
            .andExpect(view().name("airport"))
            .andExpect(model().attributeExists("countries", "form", "googleMapsApiKey", "mapData"))
            .andExpect(model().attribute("airports", hasItem(FRA)));
    }

    @Test
    public void onlyAirportsWithLocationArePutOnMap() throws Exception {
        Airport FRA = airport("FRA");
        FRA.setName("Frankfurt");
        FRA.setLocation(new Location(50.026421, 8.543125));
        Airport BER = airport("BER");
        when(airports.findAirports())
            .thenReturn(new HashSet<>(asList(FRA)));

        mvc.perform(post("/airport.html"))
            .andExpect(status().isOk());

        ArgumentCaptor<Object> mapData = ArgumentCaptor.forClass(Object.class);
        verify(json).writeValueAsString(mapData.capture());
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> allMapData = (List<Map<String, Object>>) mapData.getValue();
        assertThat(allMapData.size(), is(1));
        Map<String, Object> fraMapData = allMapData.get(0);
        assertThat(fraMapData.get("label"), is('F'));
        assertThat(fraMapData.get("title"), is("Frankfurt (FRA)"));
        assertThat((Map<String, Double>) fraMapData.get("position"), allOf(hasEntry("lat", 50.026421), hasEntry("lng", 8.543125)));
    }

    @Test
    public void oneAirportIsRequestedOnPostWithIataCode() throws Exception {
        Airport FRA = airport("FRA");
        when(airports.getAirport(anyString()))
            .thenReturn(FRA);

        mvc.perform(post("/airport.html").param("iataCode", "FRA"))
            .andExpect(status().isOk())
            .andExpect(view().name("airport"))
            .andExpect(model().attributeExists("countries", "form", "googleMapsApiKey", "mapData"))
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
            .andExpect(model().attributeExists("countries", "form", "googleMapsApiKey", "mapData"))
            .andExpect(model().attribute("airports", hasItem(FRA)));
    }

    @Before
    public void createMvc() throws JsonProcessingException {
        MockitoAnnotations.initMocks(this);
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
        conversionService.addConverter(new String2CountryConverter());
        controller.googleMapsApiKey = "ABCD";
        mvc = MockMvcBuilders.standaloneSetup(controller).setConversionService(conversionService).build();
        
        when(json.writeValueAsString(any(Collection.class)))
            .thenReturn("[]");
    }
}
