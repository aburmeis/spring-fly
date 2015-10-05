package com.tui.fly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.StandardEnvironment;

import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;
import static org.springframework.util.StringUtils.collectionToDelimitedString;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    public static void main(String... args) {
        new Application().riseAndRuleWithSpringBoot(args);
    }

    protected void riseAndRuleWithSpringBoot(final String... args)
    {
        SpringApplication app = new SpringApplication(Application.class);
        ConfigurableApplicationContext context = app.run(args);
        logger.info("Started using profiles " + asList(context.getEnvironment().getActiveProfiles()));
    }
}
