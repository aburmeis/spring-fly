package com.tui.fly;

import com.tui.fly.domain.Airport;
import com.tui.fly.service.AirportRegistry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Application {

    static AirportRegistry airports;

    static {
        try {
            airports = new AirportRegistry();
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
                            doAirports(words);
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

    private static void doAirports(String[] words) {
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

    }

    private static void doConnections(String[] words) {

    }
}