package com.analysis.SalaryStratos.features;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCache<K, V> extends LinkedHashMap<K, V> {
    //Used for search frequency
    //The LRUCache class extends LinkedHashMap and overrides the removeEldestEntry method to control when the eldest (least recently used) entry should be removed.
    // The SearchFrequency class then uses this LRUCache to maintain the search frequency map with a limited size.
    // Adjust the maxEntries parameter in the LRUCache constructor based on your requirements.
    private final int maxEntries;

    public LRUCache(int maxEntries) {
        super(maxEntries, 0.75f, true);
        this.maxEntries = maxEntries;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > maxEntries;
    }
}
