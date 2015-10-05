package com.tui.fly;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
@WebAppConfiguration
@EnableAutoConfiguration
@EnableConfigurationProperties
public abstract class AbstractConfigTest {
    
    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void contextIsFullyConfigured() {
        Application application = applicationContext.getBean(Application.class);
        assertThat(application, notNullValue());
    }
}
