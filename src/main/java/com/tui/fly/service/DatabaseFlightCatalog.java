package com.tui.fly.service;

import com.tui.fly.domain.Airport;
import com.tui.fly.domain.Connection;
import com.tui.fly.domain.Flight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static com.tui.fly.domain.Airline.airline;

@Repository
@Profile("database")
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
    @Cacheable(value = "destinations", condition = "#maxStops < 2")
    public Set<Airport> findDestinations(Airport departure, int maxStops) {
        Set<Airport> destinations = new LinkedHashSet<>(jdbc.query("SELECT destination FROM flight WHERE departure = ?", new Object[] {departure.getIataCode()}, new DestinationMapper()));
        if (maxStops > 0) {
            for (Airport destination : new HashSet<>(destinations)) {
                destinations.addAll(findDestinations(destination, maxStops - 1));
            }
        }
        log.debug("Found {} destinations for {} with up to {} stops", destinations.size(), departure, maxStops);
        return destinations;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "connections", condition = "#maxStops < 2")
    public List<Connection> findConnections(Airport departure, Airport destination, int maxStops) {
        List<Connection> connections = new ArrayList<>();
        for (Flight direct : jdbc.query("SELECT * FROM flight WHERE departure = ? AND destination = ?", new Object[] {departure.getIataCode(), destination.getIataCode()}, new FlightMapper())) {
            connections.add(new Connection(direct));
        }
        if (maxStops > 0) {
            List<Connection> starts = new ArrayList<>();
            Set<Airport> stops = new HashSet<>();
            for (Flight direct : jdbc.query("SELECT * FROM flight WHERE departure = ? AND destination <> ?", new Object[] {departure.getIataCode(), destination.getIataCode()}, new FlightMapper())) {
                starts.add(new Connection(direct));
                stops.add(direct.getTo());
            }
            for (Airport stop : stops) {
                List<Connection> furthers = findConnections(stop, destination, maxStops - 1);
                if (!furthers.isEmpty()) {
                    for (Connection further : furthers) {
                        for (Connection start : filterConnectionsTo(stop, starts)) {
                            connections.add(new Connection(start, further));
                        }
                    }
                }
            }
        }
        log.debug("Found {} connections from {} to {} with up to {} stops", connections.size(), departure, destination, maxStops);
        return connections;
    }

    private List<Connection> filterConnectionsTo(Airport stop, List<Connection> all) {
        ArrayList<Connection> filtered = new ArrayList<>();
        for (Connection candidate : all) {
            if (stop.equals(candidate.getTo())) {
                filtered.add(candidate);
            }
        }
        return filtered;
    }

    private class DestinationMapper implements RowMapper<Airport> {

        @Override
        public Airport mapRow(ResultSet rs, int rowNum) throws SQLException {
            return airports.getAirport(rs.getString("destination"));
        }
    }

    private class FlightMapper implements  RowMapper<Flight> {

        @Override
        public Flight mapRow(ResultSet rs, int rowNum) throws SQLException {
            Flight flight = new Flight(airline(rs.getString("carrier")), rs.getInt("flight_no"));
            flight.setFrom(airports.getAirport(rs.getString("departure")));
            flight.setTo(airports.getAirport(rs.getString("destination")));
            return flight;
        }
    }
}
