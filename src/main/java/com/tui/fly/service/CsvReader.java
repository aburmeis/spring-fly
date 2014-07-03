package com.tui.fly.service;

import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.NoSuchElementException;

class CsvReader implements Iterable<String[]> {

    private final Resource in;

    public CsvReader(Resource in) {
        this.in = in;
    }

    @Override
    public Iterator<String[]> iterator() {
        try {
            return new CsvIterator(new BufferedReader(new InputStreamReader(in.getInputStream(), Charset.forName("UTF-8"))));
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read CSV data " + in, e);
        }
    }

    private static class CsvIterator implements Iterator<String[]> {

        private final BufferedReader in;

        public CsvIterator(BufferedReader in) {
            this.in = in;
        }

        @Override
        public boolean hasNext() {
            try {
                return in.ready();
            } catch (IOException e) {
                return false;
            }
        }

        @Override
        public String[] next() {
            try {
                while (true) {
                    String row = in.readLine();
                    if (row == null) {
                        in.close();
                        throw new NoSuchElementException("No more CSV data");
                    }
                    if (row.startsWith("#")) {
                        continue;
                    }
                    String[] columns = row.split(" *, *");
                    for (int c = 0; c < columns.length; ++c) {
                        if ("null".equals(columns[c])) {
                            columns[c] = null;
                        }
                    }
                    return columns;
                }
            } catch (IOException e) {
                throw new NoSuchElementException("Cannot read next row");
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
