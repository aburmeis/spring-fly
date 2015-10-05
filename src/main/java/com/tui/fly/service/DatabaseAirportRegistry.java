package com.tui.fly.service;

import com.tui.fly.domain.Airport;
import com.tui.fly.domain.Country;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import static com.tui.fly.domain.Airport.airport;
import static com.tui.fly.domain.Country.country;

@Repository
@Profile("database")
class DatabaseAirportRegistry implements AirportRegistry {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final JdbcOperations jdbc;

    @Autowired
    public DatabaseAirportRegistry(JdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Airport> findAirports() {
        List<Airport> airports = jdbc.query("SELECT * FROM airport", new AirportMapper());
        log.debug("Found {} airports", airports.size());
        return new LinkedHashSet<>(airports);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Airport> findAirports(Country country) {
        List<Airport> airportsOfCountry = jdbc.query("SELECT * FROM airport WHERE country = ?", new Object[]{country.getIsoCode()}, new AirportMapper());
        log.debug("Found {} airports in {}", airportsOfCountry.size(), country);
        return new LinkedHashSet<>(airportsOfCountry);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "airports")
    public Airport getAirport(String iataCode) {
        try {
            Airport airport = jdbc.queryForObject("SELECT * FROM airport WHERE iata_code = ?", new Object[]{iataCode}, new AirportMapper());
            log.debug("Found airport {}", airport);
            return airport;
        } catch (IncorrectResultSizeDataAccessException notFound) {
            throw new NoSuchElementException("Unknown airport " + iataCode);
        }
    }

    private static class AirportMapper implements RowMapper<Airport> {

        @Override
        public Airport mapRow(ResultSet rs, int rowNum) throws SQLException {
            Airport airport = airport(rs.getString("iata_code"));
            airport.setCountry(country(rs.getString("country")));
            return airport;
        }
    }
}
