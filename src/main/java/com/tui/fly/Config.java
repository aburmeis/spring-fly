package com.tui.fly;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@ComponentScan
@EnableWebMvc
class Config {

    @Bean
    @Profile("memory")
    public Resource flightData() {
        return new ClassPathResource("flights.csv");
    }

    @Bean
    @Profile("memory")
    public Resource airportData() {
        return new ClassPathResource("airports.csv");
    }
}
