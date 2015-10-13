package com.tui.fly.domain;

import org.junit.Test;

import java.util.Locale;

import static com.tui.fly.domain.Country.country;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;

public class CountryTest {

    @Test
    public void converterIsNullSafe() {
        assertThat(country(null), nullValue());
    }

    @Test
    public void validCodeBecomesCountry() {
        assertThat(country("DE").getIsoCode(), is("DE"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidCodeRaisesError() {
        country("bla");
    }

    @Test
    public void knownReservedCodeIsTranslated() {
        assertThat(country("UK"), is(country("GB")));
    }
    
    @Test
    public void allReservedCodesAreAvailable() {
        assertThat(country("UK").getIsoCodes(), hasItems("GB", "UK", "AC", "TA"));
    }

    @Test
    public void localizedNameIsAvailable() {
        assertThat(country("UK").getName(Locale.GERMANY), is("Vereinigtes Königreich"));
        assertThat(country("DE").getName(Locale.US), is("Germany"));
    }
}
