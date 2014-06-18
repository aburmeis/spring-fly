package com.tui.fly.cli;

import com.tui.fly.domain.Airport;
import com.tui.fly.service.AirportRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("airports")
class AirportsCommand extends AbstractCommand {

    private final AirportRegistry airports;

    @Autowired
    public AirportsCommand(AirportRegistry airports) {
        this.airports = airports;
    }

    @Override
    public String execute(String... args) {
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (Airport airport : airports.findAirports()) {
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
