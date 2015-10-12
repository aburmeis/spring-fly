package com.tui.fly.web;

import com.tui.fly.domain.Airport;
import com.tui.fly.domain.Country;
import com.tui.fly.service.AirportRegistry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/rest/airport")
public class AirportController {

    @Autowired
    private AirportRegistry registry;

    @RequestMapping("")
    public Map<String,String> getApi() {
        HashMap<String, String> api = new HashMap<>();
        api.put("/{iataCode}", "get all data of the airport with the three letter IATA code");
        api.put("/search", "search for airports");
        return api;
    }

    @RequestMapping("/{iataCode}")
    public Airport getAirport(@PathVariable String iataCode) {
        return registry.getAirport(iataCode);
    }

    @RequestMapping("/search")
    public Set<Airport> getAirports(@RequestParam(required = false) String country) {
        return country == null ? registry.findAirports() : registry.findAirports(Country.country(country));
    }
}
