package com.tui.fly;

import org.junit.Test;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public abstract class AbstractConfigTest extends AbstractJUnit4SpringContextTests {

    @Test
    public void contextIsFullyConfigured() {
        Application application = applicationContext.getBean(Application.class);
        assertThat(application.commands.size(), is(3));
    }
}
