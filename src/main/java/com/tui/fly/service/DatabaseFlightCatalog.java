package com.tui.fly.service;

import com.tui.fly.domain.Airport;
import com.tui.fly.domain.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Repository
@Profile("relational")
class DatabaseFlightCatalog implements FlightCatalog {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final AirportRegistry airports;
    private final JdbcOperations jdbc;

    @Autowired
    public DatabaseFlightCatalog(JdbcOperations jdbc, AirportRegistry airports) {
        this.jdbc = jdbc;
        this.airports = airports;
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Airport> findDestinations(Airport departure, int maxStops) {
        Set<Airport> destinations = new LinkedHashSet<>(jdbc.query("SELECT destination FROM flight WHERE departure = ?", new Object[] {departure.getIataCode()}, new DestinationMapper()));
        if (maxStops > 0) {
            for (Airport destination : new HashSet<>(destinations)) {
                destinations.addAll(findDestinations(destination, maxStops - 1));
            }
        }
        return destinations;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Connection> findConnections(Airport departure, Airport destination, int maxStops) {
        throw new UnsupportedOperationException(); //TODO implement
    }

    private class DestinationMapper implements RowMapper<Airport> {

        @Override
        public Airport mapRow(ResultSet rs, int rowNum) throws SQLException {
            return airports.getAirport(rs.getString("destination"));
        }
    }
}