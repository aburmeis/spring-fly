package com.tui.fly;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import static org.junit.Assert.assertNotNull;

@ContextConfiguration(locations = "classpath:context.xml")
public class XmlConfigTest extends AbstractJUnit4SpringContextTests {

    @Test
    public void contextIsFullyConfigured() {
        Application application = applicationContext.getBean(Application.class);
        assertNotNull(application);
    }
}