package com.tui.fly;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ActiveProfiles("database")
@TestPropertySource({"classpath:application.properties", "classpath:application-database.properties"})
public class IntegrationTest extends AbstractConfigTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @Before
    public void createMvc() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }
}
