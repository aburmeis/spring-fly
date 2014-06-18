package com.tui.fly.cli;

import com.tui.fly.domain.Connection;
import com.tui.fly.service.AirportRegistry;
import com.tui.fly.service.FlightCatalog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

@Component("connections")
class ConnectionsCommand extends AbstractCommand {

    private final AirportRegistry airports;
    private final FlightCatalog flights;

    @Autowired
    ConversionService conversion;

    @Autowired
    public ConnectionsCommand(AirportRegistry airports, FlightCatalog flights) {
        this.airports = airports;
        this.flights = flights;
    }

    @Override
    public String execute(String... args) {
        StringBuilder builder = new StringBuilder();
        String departure, destination;
        int maxStops = 1;
        switch (args.length) {
            case 4:
                maxStops = Integer.parseInt(args[3]);
            case 3:
                departure = args[1];
                destination = args[2];
                boolean found = false;
                for (Connection connection : flights.findConnections(airports.getAirport(departure), airports.getAirport(destination), maxStops)) {
                    if (found) {
                        builder.append('\n');
                    } else {
                        found = true;
                    }
                    builder.append(conversion.convert(connection, String.class));
                }
                if (!found) {
                    builder.append("No connections");
                }
                break;
            default:
                throw new IllegalArgumentException("Usage: " + command + " <departureAirport> <destinationAirport> [<maxStops>]");
        }
        return builder.toString();
    }
}
