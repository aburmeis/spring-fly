package com.tui.fly.service;

import com.tui.fly.domain.Airport;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;

import static com.tui.fly.domain.Airport.airport;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.*;
import static org.mockito.AdditionalMatchers.aryEq;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DatabaseFlightCatalogTest {

    public static final Airport FRA = airport("FRA");
    public static final Airport LHR = airport("LHR");
    public static final Airport JFK = airport("JFK");
    public static final Airport MIA = airport("MIA");
    public static final Airport SFO = airport("SFO");

    private DatabaseFlightCatalog catalog;
    private JdbcOperations jdbc;

    @Test
    public void directConnectionsAreFound() {
        when(jdbc.query(anyString(), any(Object[].class), any(RowMapper.class)))
                .thenReturn(asList(LHR, JFK));

        assertThat(catalog.findDestinations(FRA, 0), hasItems(LHR, JFK));

        verify(jdbc).query(anyString(), aryEq(new Object[]{FRA.getIataCode()}), any(RowMapper.class));
    }

    @Test
    public void indirectConnectionsAreFoundByRecursion() {
        when(jdbc.query(anyString(), any(Object[].class), any(RowMapper.class)))
                .thenReturn(asList(LHR, JFK), asList(JFK, SFO), asList(SFO, MIA));

        assertThat(catalog.findDestinations(FRA, 1), hasItems(LHR, JFK, SFO, MIA));

        verify(jdbc).query(anyString(), aryEq(new Object[]{FRA.getIataCode()}), any(RowMapper.class));
        verify(jdbc).query(anyString(), aryEq(new Object[]{LHR.getIataCode()}), any(RowMapper.class));
        verify(jdbc).query(anyString(), aryEq(new Object[]{JFK.getIataCode()}), any(RowMapper.class));
    }

    @Before
    public void createRepository() {
        jdbc = mock(JdbcOperations.class);
        catalog = new DatabaseFlightCatalog(jdbc, mock(DatabaseAirportRegistry.class));
    }

}