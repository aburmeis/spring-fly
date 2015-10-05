package com.tui.fly.service;

import com.tui.fly.domain.Airport;
import com.tui.fly.domain.Country;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;

import java.util.NoSuchElementException;

import static com.tui.fly.domain.Airport.airport;
import static com.tui.fly.domain.Country.country;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.AdditionalMatchers.aryEq;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class DatabaseAirportRegistryTest {

    public static final Airport FRA = airport("FRA");
    public static final Airport LHR = airport("LHR");
    public static final Country GERMANY = country("DE");

    private DatabaseAirportRegistry registry;
    private JdbcOperations jdbc;

    @Test
    public void allAirportsAreFound() {
        when(jdbc.query(anyString(), any(RowMapper.class)))
                .thenReturn(asList(FRA, LHR));

        assertThat(registry.findAirports(), hasItems(FRA, LHR));

        verify(jdbc).query(anyString(), any(RowMapper.class));
    }

    @Test
    public void airportsOfCountryAreFound() {
        when(jdbc.query(anyString(), any(Object[].class), any(RowMapper.class)))
                .thenReturn(asList(FRA));

        assertThat(registry.findAirports(GERMANY), hasItems(FRA));

        verify(jdbc).query(anyString(), aryEq(new Object[]{GERMANY.getIsoCode()}), any(RowMapper.class));
    }

    @Test
    public void singleAirportIsFound() {
        when(jdbc.queryForObject(anyString(), any(Object[].class), any(RowMapper.class)))
                .thenReturn(FRA);

        assertThat(registry.getAirport(FRA.getIataCode()), is(FRA));

        verify(jdbc).queryForObject(anyString(), aryEq(new Object[]{FRA.getIataCode()}), any(RowMapper.class));
    }

    @Test(expected = NoSuchElementException.class)
    public void unknownAirportRaisesError() {
        when(jdbc.queryForObject(anyString(), any(Object[].class), any(RowMapper.class)))
                .thenThrow(new IncorrectResultSizeDataAccessException(1, 0));

        registry.getAirport("NIX");
    }

    @Before
    public void createRepository() {
        jdbc = mock(JdbcOperations.class);
        registry = new DatabaseAirportRegistry(jdbc);
    }
}
