package com.tui.fly.service;

import com.tui.fly.domain.Airport;
import com.tui.fly.domain.Connection;
import com.tui.fly.domain.Flight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.tui.fly.domain.Airline.airline;

public class FlightCatalog {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final AirportRegistry airports;
    private List<Flight> flights;
    private final InputStream data;

    public FlightCatalog(AirportRegistry airports, InputStream data) {
        this.airports = airports;
        this.data = data;
    }

    public void loadData() throws IOException {
        this.flights = new ArrayList<>();
        int no = 0;
        try (InputStream in = data) {
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
                    flight.setFrom(airports.getAirport(columns[1]));
                    flight.setTo(airports.getAirport(columns[2]));
                    flights.add(flight);
                }
            }
        }
        log.info("Read {} flights", flights.size());

    }

    public Set<Airport> findDestinations(Airport departure, int maxStops) {
        Set<Airport> destinations = new HashSet<>();
        fillDestinations(departure, maxStops, destinations);
        log.debug("Found {} destinations for {} with up to {} stops", destinations.size(), departure, maxStops);
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

    public List<Connection> findConnections(Airport departure, Airport destination, int maxStops) {
        List<Connection> connections = findConnections(Collections.<Flight>emptyList(), departure, destination, maxStops);
        log.debug("Found {} connections from {} to {} with up to {} stops", connections.size(), departure, destination, maxStops);
        return connections;
    }

    private List<Connection> findConnections(List<Flight> before, Airport departure, Airport destination, int maxStops) {
        List<Connection> connections = new ArrayList<>();
        for (Flight flight : flights) {
            if (flight.getFrom().equals(departure)) {
                if (flight.getTo().equals(destination)) {
                    List<Flight> route = new ArrayList<>(before);
                    route.add(flight);
                    connections.add(new Connection(route));
                } else if (maxStops > 0) {
                    List<Flight> nextBefore = new ArrayList<>(before);
                    nextBefore.add(flight);
                    connections.addAll(findConnections(nextBefore, flight.getTo(), destination, maxStops - 1));
                }
            }
        }
        return connections;
    }
}
