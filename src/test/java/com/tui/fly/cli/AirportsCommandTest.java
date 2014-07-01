package com.tui.fly.cli;

import com.tui.fly.domain.Country;
import com.tui.fly.service.AirportRegistry;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.LinkedHashSet;

import static com.tui.fly.domain.Airport.airport;
import static com.tui.fly.domain.Country.country;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;

public class AirportsCommandTest {

    public static final String COMMAND = "airports";
    private AirportsCommand command;
    private AirportRegistry service;

    @Test
    public void allAirportsArePrintedAsAListOfCodes() {
        Mockito.when(service.findAirports())
            .thenReturn(new LinkedHashSet<>(asList(airport("LHR"), airport("MIA"), airport("TXL"))));
        assertThat(command.execute(COMMAND), is("LHR,MIA,TXL"));
    }

    @Test
    public void airportsOfCountryArePrintedAsAListOfCodes() {
        Country germany = country("DE");
        Mockito.when(service.findAirports(eq(germany)))
                .thenReturn(new LinkedHashSet<>(asList(airport("FRA"), airport("TXL"))));
        assertThat(command.execute(COMMAND, germany.getIsoCode()), is("FRA,TXL"));
    }

    @Before
    public void createCommand() {
        service = Mockito.mock(AirportRegistry.class);
        command = new AirportsCommand(service);
    }
}
