package com.tui.fly;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@Configuration
@ComponentScan("com.tui.fly")
class Config {

    @Bean
    public Resource flightData() {
        return new ClassPathResource("flights.csv");
    }

    @Bean
    public Resource airportData() {
        return new ClassPathResource("airports.csv");
    }

}
