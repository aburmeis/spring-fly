package com.tui.fly.service;

import com.tui.fly.domain.Airport;
import com.tui.fly.domain.Flight;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.tui.fly.domain.Airline.airline;
import static com.tui.fly.domain.Airport.airport;

public class FlightCatalog {

    private final List<Flight> flights;

    public FlightCatalog() throws IOException {
        this.flights = new ArrayList<>();
        int no = 0;
        try (InputStream in = getClass().getResourceAsStream("/flights.csv")) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, Charset.forName("UTF-8")));
            String row;
            while (true) {
                row = reader.readLine();
                if (row == null) {
                    break;
                }
                String[] columns = row.trim().split(" *, *");
                if (columns.length == 3) {
                    Flight flight = new Flight(airline(columns[0]), 100 + no++);
                    flight.setFrom(airport(columns[1]));
                    flight.setTo(airport(columns[2]));
                    flights.add(flight);
                }
            }
        }
    }

    public Set<Airport> findDestinations(Airport departure, int maxStops) {
        Set<Airport> destinations = new HashSet<>();
        fillDestinations(departure, maxStops, destinations);
        return destinations;
    }

    private void fillDestinations(Airport departure, int maxStops, Set<Airport> destinations) {
        for (Flight flight : flights) {
            if (flight.getFrom().equals(departure)) {
                destinations.add(flight.getTo());
                if (maxStops > 0) {
                    fillDestinations(flight.getTo(), maxStops - 1, destinations);
                }
            }
        }
    }
}
