package com.tui.fly.domain;

import java.util.Iterator;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * A flight connection as a list of flights.
 * Two consecutive flights should have be connected by the same airport.
 */
public class Connection implements Iterable<Flight> {

    private final List<Flight> flights;

    public Connection(Flight... flights) {
        this.flights = asList(flights);
    }

    public Connection(List<Flight> flights) {
        this.flights = flights;
    }

    @Override
    public Iterator<Flight> iterator() {
        return flights.iterator();
    }
}
