package com.tui.fly.cli;

import com.tui.fly.domain.Airport;
import com.tui.fly.service.AirportRegistry;
import com.tui.fly.service.FlightCatalog;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.LinkedHashSet;

import static com.tui.fly.domain.Airport.airport;
import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

public class DestinationsCommandTest {

    public static final String COMMAND = "destinations";
    public static final Airport FRA = airport("FRA");
    public static final Airport JFK = airport("JFK");
    public static final Airport LHR = airport("LHR");

    private AirportRegistry airports;
    private FlightCatalog flights;
    private DestinationsCommand command;

    @Test
    public void singleDestinationIsReturnedAsOneCode() {
        when(airports.getAirport(any(String.class)))
            .thenAnswer(new Answer<Airport>() {

                @Override
                public Airport answer(InvocationOnMock invocation) throws Throwable {
                    return airport((String)invocation.getArguments()[0]);
                }
            });
        when(flights.findDestinations(eq(FRA), eq(2)))
            .thenReturn(singleton(JFK));
        assertThat(command.execute(COMMAND, "FRA", "2"), is("JFK"));
    }

    @Test
    public void multipleDestinationsAreReturnedAsOneLineWithSeparatedCodes() {
        when(airports.getAirport(any(String.class)))
            .thenAnswer(new Answer<Airport>() {

                @Override
                public Airport answer(InvocationOnMock invocation) throws Throwable {
                    return airport((String)invocation.getArguments()[0]);
                }
            });
        when(flights.findDestinations(eq(FRA), eq(2)))
            .thenReturn(new LinkedHashSet<>(asList(LHR,JFK)));
        assertThat(command.execute(COMMAND, "FRA", "2"), is("LHR,JFK"));
    }

    @Test
    public void noneStopIsDefault() {
        when(airports.getAirport(any(String.class)))
            .thenAnswer(new Answer<Airport>() {

                @Override
                public Airport answer(InvocationOnMock invocation) throws Throwable {
                    return airport((String)invocation.getArguments()[0]);
                }
                
            });
        when(flights.findDestinations(any(Airport.class), eq(0)))
            .thenReturn(singleton(LHR));
        assertThat(command.execute(COMMAND, "FRA"), is("LHR"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void commandNeedsAtLeastOneAdditionalArgument() {
        command.execute(COMMAND);
    }

    
    @Before
    public void createCommand() {
        airports = Mockito.mock(AirportRegistry.class);
        flights = Mockito.mock(FlightCatalog.class);
        command = new DestinationsCommand(airports, flights);
    }
}
