package com.tui.fly.domain;

import org.springframework.core.convert.converter.Converter;

import static org.springframework.util.StringUtils.hasText;

public class String2CountryConverter implements Converter<String,Country> {

    @Override
    public Country convert(String source) {
        return hasText(source) ? Country.country(source.trim()) : null;
    }
}
