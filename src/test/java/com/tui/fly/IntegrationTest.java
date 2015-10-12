package com.tui.fly;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasItems;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@ActiveProfiles("database")
@TestPropertySource({"classpath:application.properties", "classpath:application-database.properties"})
public class IntegrationTest extends AbstractConfigTest {

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
        mvc = webAppContextSetup(context).build();
    }
}
