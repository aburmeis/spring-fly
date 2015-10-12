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
import java.util.NoSuchElementException;
import java.util.Set;

import static com.tui.fly.domain.Airport.airport;
import static java.lang.Double.parseDouble;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.StreamSupport.stream;

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
        airports = stream(new CsvReader(data).spliterator(), false)
            .filter(columns -> columns.length > 0)
            .map(columns -> {
                    try {
                        return parseAirport(columns);
                    } catch (RuntimeException invalid) {
                        log.warn("Skipping invalid data {}: {}", asList(columns), invalid);
                        return null;
                    }
                }
            )
            .filter(airport -> airport != null)
            .collect(toSet());
        log.info("Read {} airports", airports.size());
    }
    
    @Override
    public Set<Airport> findAirports() {
        log.info("find all {} airports", airports.size());
        return airports;
    }

    @Override
    public Set<Airport> findAirports(Country country) {
        log.info("find airports by country {}", country);
        Set<Airport> airportsOfCountry = airports.stream()
            .filter(airport -> country.equals(airport.getCountry()))
            .collect(toSet());
        log.debug("Found {} airports in {}", airportsOfCountry.size(), country);
        return airportsOfCountry;
    }

    @Override
    public Airport getAirport(String iataCode) {
        log.info("get airport by IATA code {}", iataCode);
        return airports.stream()
            .filter(airport -> airport.getIataCode().equals(iataCode))
            .findAny().orElseThrow(() -> new NoSuchElementException("Unknown airport " + iataCode));
    }

    private Airport parseAirport(String[] columns) {
        Airport airport = airport(columns[0]);
        if (columns.length > 1) {
            airport.setName(columns[1]);
        }
        if (columns.length > 2) {
            airport.setCountry(Country.country(columns[2]));
        }
        if (columns.length > 4) {
            String latitude = columns[3];
            String longitude = columns[4];
            if (latitude != null && longitude != null) {
                airport.setLocation(new Location(parseDouble(latitude), parseDouble(longitude)));
            }
        }
        return airport;
    }
}
