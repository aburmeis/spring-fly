package com.tui.fly.domain;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertSame;

public class InstanceCacheTest {

    private InstanceCache<String, Object> cache;

    @Test
    public void newInstanceIsReturned() {
        Object value = new Object();
        assertSame(value, cache.getCached("test", value));
    }

    @Test
    public void oldInstanceIsReturnedIfPresent() {
        Object value = new Object();
        cache.getCached("test", value);
        assertSame(value, cache.getCached("test", new Object()));
    }

    @Test
    public void unreferencedValuesAreRemoved() {
        for (int i = 0; i < 100; ++i) {
            cache.getCached("test" + i, new Object());
        }
        System.gc();
        for (int i = 0; i < 100; ++i) {
            Object value = new Object();
            assertSame(value, cache.getCached("test" + i, value));
        }
    }

    @Before
    public void createCache() {
        cache = new InstanceCache<>();
    }
}