package com.tui.fly.service;

import com.tui.fly.domain.Airport;
import com.tui.fly.domain.Connection;
import com.tui.fly.domain.Flight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

import static com.tui.fly.domain.Airline.airline;
import static java.util.Arrays.asList;

@Service
@Profile("memory")
class InMemoryFlightCatalog implements InitializingBean, FlightCatalog {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final AirportRegistry airports;
    private List<Flight> flights;
    private final Resource data;

    @Autowired
    public InMemoryFlightCatalog(AirportRegistry airports, @Qualifier("flightData") Resource data) {
        this.airports = airports;
        this.data = data;
    }

    @Override
    public void afterPropertiesSet() throws IOException {
        this.flights = new ArrayList<>();
        int no = 0;
        for (String[] columns : new CsvReader(data)) {
            if (columns.length == 3) {
                int number = 100 + no++;
                try {
                    Flight flight = parseFlight(columns, number);
                    flights.add(flight);
                } catch (RuntimeException invalid) {
                    log.warn("Skipping invalid data {}: {}", asList(columns), invalid);
                }
            }
        }
        log.info("Read {} flights", flights.size());
    }

    private Flight parseFlight(String[] columns, int number) {
        Flight flight = new Flight(airline(columns[0]), number);
        flight.setFrom(airports.getAirport(columns[1]));
        flight.setTo(airports.getAirport(columns[2]));
        return flight;
    }

    @Override
    public Set<Airport> findDestinations(Airport departure, int maxStops) {
        log.info("find destinations from {} with max {} stops", departure, maxStops);
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

    @Override
    public List<Connection> findConnections(Airport departure, Airport destination, int maxStops) {
        log.info("find connections from {} to {} with max {} stops", departure, destination, maxStops);
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
