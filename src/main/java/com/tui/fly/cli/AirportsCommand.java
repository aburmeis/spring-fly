package com.tui.fly.cli;

import com.tui.fly.domain.Airport;
import com.tui.fly.service.AirportRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

import static com.tui.fly.domain.Country.country;

@Component("airports")
class AirportsCommand extends AbstractCommand {

    private final AirportRegistry airports;

    @Autowired
    public AirportsCommand(AirportRegistry airports) {
        this.airports = airports;
    }

    @Override
    public String execute(String... args) {
        Set<Airport> result;
        switch (args.length) {
            case 1:
                result = airports.findAirports();
                break;
            case 2:
                result = airports.findAirports(country(args[1]));
                break;
            default:
                throw new IllegalArgumentException("Usage: " + command + " [<country>]");
        }
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (Airport airport : result) {
            if (first) {
                first = false;
            } else {
                builder.append(',');
            }
            builder.append(airport.getIataCode());
        }
        return builder.toString();
    }
}
