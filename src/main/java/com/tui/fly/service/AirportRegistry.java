package com.tui.fly.service;

import com.tui.fly.domain.Airport;
import com.tui.fly.domain.Country;
import com.tui.fly.domain.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import static com.tui.fly.domain.Airport.airport;
import static java.lang.Double.parseDouble;

public class AirportRegistry {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final InputStream data;
    private Set<Airport> airports;

    public AirportRegistry(InputStream data) {
        this.data = data;
    }

    public void loadData() throws IOException {
        airports = new HashSet<>();
        try (InputStream in = data) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, Charset.forName("UTF-8")));
            String row;
            while (true) {
                row = reader.readLine();
                if (row == null) {
                    break;
                }
                String[] columns = row.trim().split(" *, *");
                if (columns.length > 0) {
                    Airport airport = airport(columns[0]);
                    airports.add(airport);
                    if (columns.length > 2) {
                        airport.setCountry(Country.country(columns[2]));
                    }
                    if (columns.length > 4) {
                        airport.setLocation(new Location(parseDouble(columns[3]), parseDouble(columns[4])));
                    }
                }
            }
        }
        log.info("Read {} airports", airports.size());
    }

    public Set<Airport> findAirports() {
        return airports;
    }

    public Airport getAirport(String iataCode) {
        Airport airport = airport(iataCode);
        if (!airports.contains(airport)) {
            throw new NoSuchElementException("Unknown airport " + iataCode);
        }
        return airport;
    }
}
