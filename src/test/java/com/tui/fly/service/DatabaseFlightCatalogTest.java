package com.tui.fly.service;

import com.tui.fly.domain.Airport;
import com.tui.fly.domain.Connection;
import com.tui.fly.domain.Flight;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;

import static com.tui.fly.domain.Airline.airline;
import static com.tui.fly.domain.Airport.airport;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertThat;
import static org.mockito.AdditionalMatchers.aryEq;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class DatabaseFlightCatalogTest {

    public static final Airport FRA = airport("FRA");
    public static final Airport LHR = airport("LHR");
    public static final Airport JFK = airport("JFK");
    public static final Airport MIA = airport("MIA");
    public static final Airport SFO = airport("SFO");
    public static final Flight LH100;
    public static final Flight LH200;
    public static final Flight BA200;

    static {
        LH100 = new Flight(airline("LH"), 100);
        LH100.setFrom(FRA);
        LH100.setTo(LHR);
        LH200 = new Flight(airline("LH"), 200);
        LH200.setFrom(FRA);
        LH200.setTo(JFK);
        BA200 = new Flight(airline("BA"), 200);
        BA200.setFrom(LHR);
        BA200.setTo(JFK);
    }

    private DatabaseFlightCatalog catalog;
    private JdbcOperations jdbc;

    @Test
    public void directDestinationsAreFound() {
        when(jdbc.query(anyString(), any(Object[].class), any(RowMapper.class)))
                .thenReturn(asList(LHR, JFK));

        assertThat(catalog.findDestinations(FRA, 0), hasItems(LHR, JFK));

        verify(jdbc).query(anyString(), aryEq(new Object[]{FRA.getIataCode()}), any(RowMapper.class));
    }

    @Test
    public void indirectDestinationsAreFound() {
        when(jdbc.query(anyString(), any(Object[].class), any(RowMapper.class)))
                .thenReturn(asList(LHR, JFK), asList(JFK, SFO), asList(SFO, MIA));

        assertThat(catalog.findDestinations(FRA, 1), hasItems(LHR, JFK, SFO, MIA));

        verify(jdbc).query(anyString(), aryEq(new Object[]{FRA.getIataCode()}), any(RowMapper.class));
        verify(jdbc).query(anyString(), aryEq(new Object[]{LHR.getIataCode()}), any(RowMapper.class));
        verify(jdbc).query(anyString(), aryEq(new Object[]{JFK.getIataCode()}), any(RowMapper.class));
    }

    @Test
    public void directConnectionsAreFound() {
        when(jdbc.query(anyString(), any(Object[].class), any(RowMapper.class)))
                .thenReturn(asList(LH100, BA200));

        assertThat(catalog.findConnections(FRA, LHR, 0), hasItems(new Connection(LH100), new Connection(BA200)));

        verify(jdbc).query(anyString(), aryEq(new Object[]{FRA.getIataCode(), LHR.getIataCode()}), any(RowMapper.class));
    }

    @Test
    public void indirectConnectionsAreFound() {
        when(jdbc.query(anyString(), any(Object[].class), any(RowMapper.class)))
                .thenReturn(asList(LH200), asList(LH100), asList(BA200));

        assertThat(catalog.findConnections(FRA, JFK, 1), hasItems(new Connection(LH100, BA200), new Connection(LH200)));

        verify(jdbc, times(2)).query(anyString(), aryEq(new Object[]{FRA.getIataCode(), JFK.getIataCode()}), any(RowMapper.class));
        verify(jdbc).query(anyString(), aryEq(new Object[]{LHR.getIataCode(), JFK.getIataCode()}), any(RowMapper.class));
    }

    @Before
    public void createRepository() {
        jdbc = mock(JdbcOperations.class);
        catalog = new DatabaseFlightCatalog(jdbc, mock(DatabaseAirportRegistry.class));
    }

}
