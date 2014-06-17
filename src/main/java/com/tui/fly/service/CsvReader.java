package com.tui.fly.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.NoSuchElementException;

class CsvReader implements Iterable<String[]> {

    private final InputStream in;

    public CsvReader(InputStream in) {
        this.in = in;
    }

    @Override
    public Iterator<String[]> iterator() {
        return new CsvIterator(new BufferedReader(new InputStreamReader(in, Charset.forName("UTF-8"))));
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
                    return row.split(" *, *");
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