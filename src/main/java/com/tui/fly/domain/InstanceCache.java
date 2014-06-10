package com.tui.fly.domain;

import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

class InstanceCache<K, V> {

    private final ConcurrentMap<K, WeakReference<V>> instances = new ConcurrentHashMap<>();

    public V getCached(K key, V value) {
        WeakReference<V> previous = instances.putIfAbsent(key, new WeakReference<>(value));
        if (previous == null) {
            return value;
        }
        V cached = previous.get();
        if (cached == null) {
            instances.replace(key, previous, new WeakReference<>(value));
            return value;
        }
        return cached;
    }
}
