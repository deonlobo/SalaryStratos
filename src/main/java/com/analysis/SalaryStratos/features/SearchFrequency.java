package com.analysis.SalaryStratos.features;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchFrequency {

    //Uses LRUCache class
    private int CACHESIZE = 5;
    private int RETURNARRAYSIZE = 4;
    private final LRUCache<String, Integer> searchFrequencyMap = new LRUCache<>(CACHESIZE); // Adjust the cache size as needed

    public Map<String, Integer> displaySearchFrequencies() {
        Map<String, Integer> recentEntries = new LinkedHashMap<>();
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(searchFrequencyMap.entrySet());

        // Get the last 10 entries
        int startIndex = Math.max(0, entries.size() - RETURNARRAYSIZE);
        for (int i = startIndex; i < entries.size(); i++) {
            Map.Entry<String, Integer> entry = entries.get(i);
            recentEntries.put(entry.getKey(), entry.getValue());
        }

        //sort the recent entries
        // Sort recent entries based on frequency in descending order
        recentEntries = recentEntries.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        return recentEntries;
    }

    public void updateSearchFrequency(String searchTerm) {
        searchFrequencyMap.put(searchTerm, searchFrequencyMap.getOrDefault(searchTerm, 0) + 1);
    }
}
