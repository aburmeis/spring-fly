package com.tui.fly.cli;

import com.tui.fly.domain.Airport;
import com.tui.fly.service.AirportRegistry;
import com.tui.fly.service.FlightCatalog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("destinations")
class DestinationsCommand extends AbstractCommand {

    private final AirportRegistry airports;
    private final FlightCatalog flights;

    @Autowired
    public DestinationsCommand(AirportRegistry airports, FlightCatalog flights) {
        this.airports = airports;
        this.flights = flights;
    }

    @Override
    public String execute(String... args) {
        StringBuilder builder = new StringBuilder();
        String departure;
        int maxStops = 0;
        switch (args.length) {
            case 3:
                maxStops = Integer.parseInt(args[2]);
            case 2:
                departure = args[1];
                boolean first = true;
                for (Airport airport : flights.findDestinations(airports.getAirport(departure), maxStops)) {
                    if (first) {
                        first = false;
                    } else {
                        builder.append(',');
                    }
                    builder.append(airport.getIataCode());
                }
                break;
            default:
                throw new IllegalArgumentException("Usage: " + command + " <departureAirport> [<maxStops>]");
        }
        return builder.toString();
    }
}
