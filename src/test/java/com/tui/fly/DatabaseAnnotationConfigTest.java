package com.tui.fly;

import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.context.ActiveProfiles;

import java.util.Properties;

@ActiveProfiles("database")
public class DatabaseAnnotationConfigTest extends AbstractConfigTest {

    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() throws Exception {
        PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
        Properties properties = new Properties();
        properties.put("liquibase.enabled", "true");
        properties.put("liquibase.change-log", "classpath:liquibase.xml");
        pspc.setProperties(properties);
        return pspc;
    }
}
