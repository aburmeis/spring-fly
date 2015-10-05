package com.tui.fly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.core.env.StandardEnvironment;

import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;
import static org.springframework.util.StringUtils.collectionToDelimitedString;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    static final Set<String> VALID_PROFILES = new HashSet<>(asList("memory", "relational"));

    public static void main(String... args) {
        new Application().riseAndRuleWithSpringBoot(args);
    }

    protected void riseAndRuleWithSpringBoot(final String... args)
    {
        String profile = "memory";
        for (String arg : args) {
            if (VALID_PROFILES.contains(arg)) {
                profile = arg;
            } else {
                System.err.println("Usage: " + collectionToDelimitedString(VALID_PROFILES, "|", "[", "]"));
            }
        }
        final SpringApplication app = new SpringApplication(Application.class);
        StandardEnvironment env = new StandardEnvironment();
        env.setActiveProfiles(profile);
        app.setEnvironment(env);
        app.run();
        logger.info("Started using profile " + profile);
    }
}
