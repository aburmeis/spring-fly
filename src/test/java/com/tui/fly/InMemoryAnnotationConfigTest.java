package com.tui.fly;

import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.context.ActiveProfiles;

import java.util.Properties;

@ActiveProfiles("memory")
public class InMemoryAnnotationConfigTest extends AbstractConfigTest {

    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() throws Exception {
        PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
        Properties properties = new Properties();
        properties.put("liquibase.enabled", "false");
        pspc.setProperties(properties);
        return pspc;
    }
}
