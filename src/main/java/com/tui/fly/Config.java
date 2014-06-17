package com.tui.fly;

import com.tui.fly.domain.ConnectionToStringConverter;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.LinkedHashSet;
import java.util.Set;

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

    @Bean
    public FactoryBean<ConversionService> conversionService() {
        ConversionServiceFactoryBean factory = new ConversionServiceFactoryBean();
        factory.setConverters(getConverters());
        return factory;
    }

    public static Set<Converter<?,?>> getConverters() {
        Set<Converter<?, ?>> converters = new LinkedHashSet<>();
        converters.add(new ConnectionToStringConverter());
        return converters;
    }
}
