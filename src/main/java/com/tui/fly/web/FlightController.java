package com.tui.fly.web;

import com.tui.fly.domain.Airport;
import com.tui.fly.domain.Flight;
import com.tui.fly.service.AirportRegistry;
import com.tui.fly.service.FlightCatalog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

@RestController
@RequestMapping("/rest/flight")
class FlightController {

    @Autowired
    private AirportRegistry airports;
    @Autowired
    private FlightCatalog flights;

    @RequestMapping("")
    public Map<String,String> getApi() {
        HashMap<String, String> api = new HashMap<>();
        api.put("/destination/{departure}", "get all possible destinations for the airport");
        api.put("/connection/{departure}/{destination}", "get all possible connections between departure and destination airport");
        return api;
    }

    @RequestMapping("/destination/{departure}")
    public Set<Airport> findDestinations(@PathVariable String departure, @RequestParam(required = false, defaultValue = "0") int maxStops) {
        return flights.findDestinations(airports.getAirport(departure), maxStops);
    }

    @RequestMapping("/connection/{departure}/{destination}")
    public List<List<Flight>> findConnection(@PathVariable String departure, @PathVariable String destination, @RequestParam(required = false, defaultValue = "0") int maxStops) {
        return flights.findConnections(airports.getAirport(departure), airports.getAirport(destination), maxStops).stream()
            .map(con -> stream(con.spliterator(), false).collect(toList()))
            .collect(toList());
    }
}
