package com.tui.fly.domain;

import org.springframework.core.convert.converter.Converter;

public class ConnectionToStringConverter implements Converter<Connection, String> {

    @Override
    public String convert(Connection source) {
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (Flight flight : source) {
            if (first) {
                builder.append(flight.getFrom().getIataCode());
                first = false;
            }
            builder.append(' ');
            builder.append(flight.getCarrier().getIataCode());
            builder.append(String.valueOf(flight.getNumber()));
            builder.append(' ');
            builder.append(flight.getTo().getIataCode());
        }
        return builder.toString();
    }
}
