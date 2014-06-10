package com.tui.fly.domain;

/**
 * A flight identified by the carrier and its flight number.
 */
public class Flight {

    private final Airline carrier;
    private final int number;
    private Airport from;
    private Airport to;

    public Flight(Airline carrier, int number) {
        if (carrier == null) {
            throw new IllegalArgumentException("Missing carrier");
        }
        if (number <= 0) {
            throw new IllegalArgumentException("Invalid flight no: " + number);
        }
        this.carrier = carrier;
        this.number = number;
    }

    public Airline getCarrier() {
        return carrier;
    }

    public int getNumber() {
        return number;
    }

    public Airport getFrom() {
        return from;
    }

    public void setFrom(Airport from) {
        this.from = from;
    }

    public Airport getTo() {
        return to;
    }

    public void setTo(Airport to) {
        this.to = to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Flight flight = (Flight)o;

        if (number != flight.number) {
            return false;
        }
        if (!carrier.equals(flight.carrier)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = carrier.hashCode();
        result = 31 * result + number;
        return result;
    }

    @Override
    public String toString() {
        return carrier.toString() + ' ' + number;
    }
}
