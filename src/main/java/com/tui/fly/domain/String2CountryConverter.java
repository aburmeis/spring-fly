package com.tui.fly.domain;

import org.springframework.core.convert.converter.Converter;

public class String2CountryConverter implements Converter<String,Country> {

    @Override
    public Country convert(String source) {
        return source == null ? null : Country.country(source);
    }
}
