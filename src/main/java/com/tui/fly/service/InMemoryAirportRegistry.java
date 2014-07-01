package com.tui.fly.service;

import com.tui.fly.domain.Airport;
import com.tui.fly.domain.Country;
import com.tui.fly.domain.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import static com.tui.fly.domain.Airport.airport;
import static java.lang.Double.parseDouble;
import static java.util.Arrays.asList;

@Service
@Profile("memory")
class InMemoryAirportRegistry implements InitializingBean, AirportRegistry {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final Resource data;
    private Set<Airport> airports;

    @Autowired
    public InMemoryAirportRegistry(@Qualifier("airportData") Resource data) {
        this.data = data;
    }

    @Override
    public void afterPropertiesSet() throws IOException {
        airports = new HashSet<>();
        for (String[] columns : new CsvReader(data)) {
            if (columns.length > 0) {
                try {
                    Airport airport = parseAirport(columns);
                    airports.add(airport);
                } catch (RuntimeException invalid) {
                    log.warn("Skipping invalid data {}: {}", asList(columns), invalid);
                }
            }
        }
        log.info("Read {} airports", airports.size());
    }

    @Override
    public Set<Airport> findAirports() {
        return airports;
    }

    @Override
    public Set<Airport> findAirports(Country country) {
        HashSet<Airport> airportsOfCountry = new HashSet<>();
        for (Airport candidate : airports) {
            if (country.equals(candidate.getCountry())) {
                airportsOfCountry.add(candidate);
            }
        }
        log.debug("Found {} airports in {}", airportsOfCountry.size(), country);
        return airportsOfCountry;
    }

    @Override
    public Airport getAirport(String iataCode) {
        Airport airport = airport(iataCode);
        if (!airports.contains(airport)) {
            throw new NoSuchElementException("Unknown airport " + iataCode);
        }
        return airport;
    }

    private Airport parseAirport(String[] columns) {
        Airport airport = airport(columns[0]);
        if (columns.length > 2) {
            airport.setCountry(Country.country(columns[2]));
        }
        if (columns.length > 4) {
            airport.setLocation(new Location(parseDouble(columns[3]), parseDouble(columns[4])));
        }
        return airport;
    }
}
