package com.tui.fly.web;

import com.tui.fly.domain.Airport;
import com.tui.fly.domain.Country;
import com.tui.fly.service.AirportRegistry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.Collections.singleton;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.StringUtils.hasText;

@Controller
@RequestMapping("/")
class FormController {

    private static final Logger LOG = LoggerFactory.getLogger(FlightController.class);

    @Autowired
    private AirportRegistry airports;

    @ModelAttribute("countries")
    public List<Country> populateCountries() {
        return Stream.of(Locale.getISOCountries()).map(Country::country).collect(toList());
    }

    @RequestMapping(value = "index", method = RequestMethod.GET)
    public ModelAndView showForm(ModelMap model, @ModelAttribute("form") AirportSearch form) {
        LOG.info("show form {}", form);
        return new ModelAndView("index", model);
    }

    @RequestMapping(value = "airport")
    public ModelAndView findAirports(ModelMap model, @ModelAttribute("form") AirportSearch form) {
        LOG.info("find airports {}", form);
        Set<Airport> found;
        if (hasText(form.getIataCode())) {
            found = singleton(airports.getAirport(form.getIataCode().trim()));
        } else if (form.getCountry() != null) {
            found = airports.findAirports(form.getCountry());
        } else {
            found = airports.findAirports();
        }
        model.addAttribute("airports", found);
        LOG.debug("airport model {}", model);
        return new ModelAndView("airport", model);
    }
}
