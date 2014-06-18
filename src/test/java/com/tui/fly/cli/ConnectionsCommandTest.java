package com.tui.fly.cli;

import com.tui.fly.domain.Airport;
import com.tui.fly.domain.Connection;
import com.tui.fly.domain.Flight;
import com.tui.fly.service.AirportRegistry;
import com.tui.fly.service.FlightCatalog;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.core.convert.ConversionService;

import static com.tui.fly.domain.Airline.airline;
import static com.tui.fly.domain.Airport.airport;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

public class ConnectionsCommandTest {

    public static final String COMMAND = "connections";
    public static final Airport FRA = airport("FRA");
    public static final Airport JFK = airport("JFK");
    public static final Connection CONNECTION = new Connection(new Flight(airline("LH"), 100));

    private AirportRegistry airports;
    private FlightCatalog flights;
    private ConversionService conversionService;
    private ConnectionsCommand command;
    
    @Test
    public void singleConnectionIsReturnedAsOneConvertedLine() {
        when(airports.getAirport(any(String.class)))
            .thenAnswer(new Answer<Airport>() {

                @Override
                public Airport answer(InvocationOnMock invocation) throws Throwable {
                    return airport((String)invocation.getArguments()[0]);
                }
            });
        when(flights.findConnections(eq(FRA), eq(JFK), eq(2)))
            .thenReturn(singletonList(CONNECTION));
        when(conversionService.convert(any(Connection.class), eq(String.class)))
            .thenReturn("FRA-JFK");
        assertThat(command.execute(COMMAND, "FRA", "JFK", "2"), is("FRA-JFK"));
    }

    @Test
    public void oneStopIsDefault() {
        when(airports.getAirport(any(String.class)))
            .thenAnswer(new Answer<Airport>() {

                @Override
                public Airport answer(InvocationOnMock invocation) throws Throwable {
                    return airport((String)invocation.getArguments()[0]);
                }
            });
        when(flights.findConnections(any(Airport.class), any(Airport.class), eq(1)))
            .thenReturn(singletonList(CONNECTION));
        when(conversionService.convert(any(Connection.class), eq(String.class)))
            .thenReturn("FRA-JFK");
        assertThat(command.execute(COMMAND, "FRA", "JFK"), is("FRA-JFK"));
    }

    @Test
    public void multipleConnectionsAreReturnedAsTwoConvertedLines() {
        when(airports.getAirport(any(String.class)))
            .thenAnswer(new Answer<Airport>() {

                @Override
                public Airport answer(InvocationOnMock invocation) throws Throwable {
                    return airport((String)invocation.getArguments()[0]);
                }
            });
        when(flights.findConnections(any(Airport.class), any(Airport.class), anyInt()))
            .thenReturn(asList(CONNECTION, CONNECTION));
        when(conversionService.convert(any(Connection.class), eq(String.class)))
            .thenReturn("FRA-JFK");
        assertThat(command.execute(COMMAND, "FRA", "JFK", "1"), is("FRA-JFK\nFRA-JFK"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void commandNeedsAtLeastTwoAdditionalArguments() {
        command.execute(COMMAND, "HU");
    }
    
    @Before
    public void createCommand() {
        airports = Mockito.mock(AirportRegistry.class);
        flights = Mockito.mock(FlightCatalog.class);
        command = new ConnectionsCommand(airports, flights);
        conversionService = Mockito.mock(ConversionService.class);
        command.conversion = conversionService;
    }
}
