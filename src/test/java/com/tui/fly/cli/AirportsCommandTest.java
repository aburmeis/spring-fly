package com.tui.fly.cli;

import com.tui.fly.service.AirportRegistry;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.LinkedHashSet;

import static com.tui.fly.domain.Airport.airport;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class AirportsCommandTest {

    private AirportsCommand command;
    private AirportRegistry service;

    @Test
    public void airportsArePrintedAsAListOfCodes() {
        Mockito.when(service.findAirports())
            .thenReturn(new LinkedHashSet<>(asList(airport("LHR"), airport("MIA"), airport("TXL"))));
        assertThat(command.execute(), is("LHR,MIA,TXL"));
    }

    @Before
    public void createCommand() {
        service = Mockito.mock(AirportRegistry.class);
        command = new AirportsCommand(service);
    }
}
