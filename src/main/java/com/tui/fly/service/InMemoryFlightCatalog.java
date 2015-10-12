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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static com.tui.fly.domain.Airline.airline;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

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
        AtomicInteger number = new AtomicInteger(100);
        flights = stream(new CsvReader(data).spliterator(), false)
            .filter(columns -> columns.length == 3)
            .map(columns -> {
                try {
                    return parseFlight(columns, number.getAndIncrement());
                } catch (RuntimeException invalid) {
                    log.warn("Skipping invalid data {}: {}", asList(columns), invalid);
                    return null;
                }
            })
            .filter(flight -> flight != null)
            .collect(toList());
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
        return flights.stream()
            .filter(flight -> flight.getFrom().equals(departure))
            .filter(flight -> maxStops > 0 || flight.getTo().equals(destination))
            .map(flight -> {
                if (flight.getTo().equals(destination)) {
                    List<Flight> route = new ArrayList<>(before);
                    route.add(flight);
                    return singletonList(new Connection(route));
                } else {
                    List<Flight> nextBefore = new ArrayList<>(before);
                    nextBefore.add(flight);
                    return findConnections(nextBefore, flight.getTo(), destination, maxStops - 1);
                }
            })
            .flatMap(Collection::stream)
            .collect(toList());
    }
}
