package com.tui.fly.service;

import com.tui.fly.domain.Airport;
import com.tui.fly.domain.Connection;

import java.util.List;
import java.util.Set;

public interface FlightCatalog {
    Set<Airport> findDestinations(Airport departure, int maxStops);

    List<Connection> findConnections(Airport departure, Airport destination, int maxStops);
}
