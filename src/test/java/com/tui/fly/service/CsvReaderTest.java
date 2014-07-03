package com.tui.fly.service;

import org.junit.Test;
import org.springframework.core.io.ByteArrayResource;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CsvReaderTest {

    @Test
    public void eachLineBecomesARowWithColumns() {
        int rows = 0;
        for (String[] columns : new CsvReader(new ByteArrayResource("a1,a2,a3\nb1,b2\nc1,c2,c3".getBytes()))) {
            assertThat(columns, anyOf(
                    is(new String[] {"a1", "a2", "a3"}),
                    is(new String[] {"b1", "b2"}),
                    is(new String[] {"c1", "c2", "c3"})));
            ++rows;
        }
        assertThat(rows, is(3));
    }

    @Test
    public void nullColumnsAreRecognized() {
        String[] columns = new CsvReader(new ByteArrayResource("a1,null,a3".getBytes())).iterator().next();
        assertThat(columns, is(new String[] {"a1", null, "a3"}));
    }

    @Test
    public void linesStartingWithHashAreComments() {
        int rows = 0;
        for (String[] columns : new CsvReader(new ByteArrayResource("# comment\na1,a2\n#b1,b2\nc1".getBytes()))) {
            assertThat(columns, anyOf(
                    is(new String[] {"a1", "a2"}),
                    is(new String[] {"c1"})));
            ++rows;
        }
        assertThat(rows, is(2));
    }
}
