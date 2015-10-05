package com.tui.fly;

import com.tui.fly.domain.ConnectionToStringConverter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@ComponentScan("com.tui.fly")
@EnableWebMvc
class Config extends WebMvcConfigurerAdapter {

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

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new ConnectionToStringConverter());
    }
}
