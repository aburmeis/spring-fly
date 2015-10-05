package com.tui.fly;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasItems;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
@ActiveProfiles("database")
@WebAppConfiguration
@EnableAutoConfiguration
//@TestPropertySource(locations="classpath:application-relational.properties")
public class IntegrationTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @Test
    public void germanAirportsAreFound() throws Exception {
        mvc.perform(get("/rest/airport/search").param("country", "DE"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$..iataCode", hasItems("FRA", "MUC")));
    }

    @Before
    public void createMvc() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }
}
