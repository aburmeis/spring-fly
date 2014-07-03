package com.tui.fly;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertThat;

@ContextConfiguration(classes = Config.class)
@ActiveProfiles("relational")
public class IntegrationTest extends AbstractJUnit4SpringContextTests {

    private InputStream originalIn;
    private PrintStream originalOut;
    private ByteArrayOutputStream testOut;

    @Test
    public void germanAirportsAreFound() {
        setTestInput("airports DE\n");
        Application application = applicationContext.getBean(Application.class);
        application.run();
        assertThat(asList(testOut.toString().trim().split(",")),
            hasItems("FRA", "MUC"));
    }

    @Test
    public void destinationsFromFrankfurtWithOneStopAreAllFound() {
        setTestInput("destinations FRA 1\n");
        Application application = applicationContext.getBean(Application.class);
        application.run();
        assertThat(asList(testOut.toString().trim().split(",")),
            hasItems("LHR","MUC","CDG","AMS","FRA","JFK","LAX","MIA","LGW"));
    }

    @Test
    public void connectionsFromFrankfurtToNewYorkWithUpTo2StopsAreAllFound() {
        setTestInput("connections FRA JFK 2\n");
        Application application = applicationContext.getBean(Application.class);
        application.run();
        assertThat(asList(testOut.toString().split("\n")),
            hasItems("FRA LH114 AMS KL125 JFK",
                "FRA LH114 AMS KL124 LHR BA102 JFK",
                "FRA LH100 LHR BA102 JFK",
                "FRA LH100 LHR BA104 LAX AA107 JFK",
                "FRA LH100 LHR BA106 AMS KL125 JFK",
                "FRA LH113 CDG AF116 JFK",
                "FRA LH113 CDG AF118 LAX AA107 JFK"));
    }

    private void setTestInput(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
    }

    @Before
    public void redirectInAndOut() {
        originalIn = System.in;
        originalOut = System.out;
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    @After
    public void restoreInAndOut() {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }
}
