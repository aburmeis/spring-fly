package com.tui.fly;

import com.tui.fly.domain.Airport;
import com.tui.fly.domain.Connection;
import com.tui.fly.domain.Flight;
import com.tui.fly.service.AirportRegistry;
import com.tui.fly.service.FlightCatalog;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;

public class Application implements Runnable {

    public static void main(String... args) {
        ConfigurableApplicationContext context = createXmlContext();
        context.refresh();
        Application application = context.getBean(Application.class);
        application.run();
        context.close();
    }

    private static ConfigurableApplicationContext createXmlContext() {
        GenericXmlApplicationContext context = new GenericXmlApplicationContext();
        context.load(new ClassPathResource("context.xml"));
        return context;
    }

    private final AirportRegistry airports;
    private final FlightCatalog flights;

    public Application(AirportRegistry airports, FlightCatalog flights) {
        this.airports = airports;
        this.flights = flights;
    }

    @Override
    public void run() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while (true) {
            try {
                line = in.readLine();
                if (line == null) {
                    return;
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
                return;
            }
        }
    }

    private void doAirports() {
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

    private void doDestinations(String[] words) {
        String departure;
        int maxStops = 0;
        switch (words.length) {
            case 3:
                maxStops = Integer.parseInt(words[2]);
            case 2:
                departure = words[1];
                try {
                    boolean first = true;
                    for (Airport airport : flights.findDestinations(airports.getAirport(departure), maxStops)) {
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

    private void doConnections(String[] words) {
        String departure, destination;
        int maxStops = 1;
        switch (words.length) {
            case 4:
                maxStops = Integer.parseInt(words[3]);
            case 3:
                departure = words[1];
                destination = words[2];
                try {
                    boolean found = false;
                    for (Connection connection : flights.findConnections(airports.getAirport(departure), airports.getAirport(destination), maxStops)) {
                        boolean first = true;
                        for (Flight flight : connection) {
                            if (first) {
                                System.out.print(flight.getFrom().getIataCode());
                                first = false;
                            }
                            System.out.print(' ');
                            System.out.print(flight.getCarrier().getIataCode());
                            System.out.print(String.valueOf(flight.getNumber()));
                            System.out.print(' ');
                            System.out.print(flight.getTo().getIataCode());
                        }
                        System.out.println();
                        found = true;
                    }
                    if (!found) {
                        System.err.println("No connections");
                    }
                } catch (NoSuchElementException unknown) {
                    System.err.println(unknown.getMessage());
                }
                break;
            default:
                System.err.println("Usage: connections <departureAirport> <destinationAirport> [<maxStops>]");
                break;
        }
    }
}