package com.tui.fly;

import com.tui.fly.domain.Airport;
import com.tui.fly.service.AirportRegistry;
import com.tui.fly.service.FlightCatalog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;

import static com.tui.fly.domain.Airport.airport;

public class Application {

    static AirportRegistry airports;
    static FlightCatalog flights;

    static {
        try {
            airports = new AirportRegistry();
            flights = new FlightCatalog();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static void main(String... args) {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while (true) {
            try {
                line = in.readLine();
                if (line == null) {
                    break;
                }
                String[] words = line.split(" +");
                if (words.length > 0) {
                    String command = words[0];
                    switch (command) {
                        case "airports":
                            doAirports();
                            break;
                        case "destinations": {
                            doDestinations(words);
                            break;
                        }
                        case "connections": {
                            doConnections(words);
                            break;
                        }
                        default:
                            System.err.println("unknown command " + command);
                            System.out.println("Commands: airports, destinations, connections");
                            break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void doAirports() {
        boolean first = true;
        for (Airport airport : airports.findAirports()) {
            if (first) {
                first = false;
            } else {
                System.out.print(',');
            }
            System.out.print(airport.getIataCode());
        }
        System.out.println();
    }

    private static void doDestinations(String[] words) {
        String departure;
        int maxStops = 0;
        switch (words.length) {
            case 3:
                maxStops = Integer.parseInt(words[2]);
            case 2:
                departure = words[1];
                try {
                    boolean first = true;
                    for (Airport airport : flights.findDestinations(airport(departure), maxStops)) {
                        if (first) {
                            first = false;
                        } else {
                            System.out.print(',');
                        }
                        System.out.print(airport.getIataCode());
                    }
                    System.out.println();
                } catch (NoSuchElementException unknown) {
                    System.err.println(unknown.getMessage());
                }
                break;
            default:
                System.err.println("Usage: destinations <departureAirport> [<maxStops>]");
                break;
        }
    }

    private static void doConnections(String[] words) {

    }
}