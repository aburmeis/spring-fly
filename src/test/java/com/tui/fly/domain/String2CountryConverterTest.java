package com.tui.fly.domain;

import org.junit.Test;

import static com.tui.fly.domain.Country.country;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class String2CountryConverterTest {
    
    private final String2CountryConverter converter = new String2CountryConverter();

    @Test
    public void converterIsNullSafe() {
        assertThat(converter.convert(null), nullValue());
    }

    @Test
    public void emptyStringBecomesNull() {
        assertThat(converter.convert(""), nullValue());
    }

    @Test
    public void validCodeBecomesCountry() {
        assertThat(converter.convert("DE"), is(country("DE")));
    }
}
