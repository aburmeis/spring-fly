package com.tui.fly.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * A flight connection as a list of flights.
 * Two consecutive flights should have be connected by the same airport.
 */
public final class Connection implements Iterable<Flight> {

    private final List<Flight> flights;

    public Connection(Flight... flights) {
        this(asList(flights));
    }

    public Connection(List<Flight> flights) {
        if (flights.isEmpty()) {
            throw new IllegalArgumentException("missing flight(s) on connection");
        }
        this.flights = flights;
    }

    public Connection(Connection... connections) {
        flights = new ArrayList<>();
        for (Connection connection : connections) {
            flights.addAll(connection.flights);
        }
    }

    public Airport getFrom() {
        return flights.get(0).getFrom();
    }

    public Airport getTo() {
        return flights.get(flights.size() - 1).getTo();
    }

    @Override
    public Iterator<Flight> iterator() {
        return flights.iterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Connection flights1 = (Connection) o;

        if (!flights.equals(flights1.flights)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return flights.hashCode();
    }

    @Override
    public String toString() {
        return flights.toString();
    }
}
