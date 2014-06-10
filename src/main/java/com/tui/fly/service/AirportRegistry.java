package com.tui.fly.service;

import com.tui.fly.domain.Airport;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import static com.tui.fly.domain.Airport.airport;

public class AirportRegistry {

    private final Map<String, Airport> airports;

    public AirportRegistry() throws IOException {
        airports = new HashMap<>();
        try (InputStream in = getClass().getResourceAsStream("/airports.csv")) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, Charset.forName("UTF-8")));
            String row;
            while (true) {
                row = reader.readLine();
                if (row == null) {
                    break;
                }
                String[] columns = row.trim().split(" *, *");
                if (columns.length > 0) {
                    airports.put(columns[0], airport(columns[0]));
                }
            }
        }

    }

    public Iterable<Airport> findAirports() {
        return airports.values();
    }
}
