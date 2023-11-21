package com.analysis.SalaryStratos.features;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.HashMap;

@Service
public class SearchFrequency {
    private Map<String, Integer> searchFrequencyMap = new HashMap<>();

    public Map<String, Integer> displaySearchFrequencies() {
        // Display search frequencies
       /* for (Map.Entry<String, Integer> entry : searchFrequencyMap.entrySet()) {
            System.out.println("Search Term: " + entry.getKey() + ", Frequency: " + entry.getValue());
        }*/
        return searchFrequencyMap;
    }

    public void updateSearchFrequency(String searchTerm) {
        searchFrequencyMap.put(searchTerm, searchFrequencyMap.getOrDefault(searchTerm, 0) + 1);
    }
}
