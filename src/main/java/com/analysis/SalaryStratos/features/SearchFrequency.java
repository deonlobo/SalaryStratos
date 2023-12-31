package com.analysis.SalaryStratos.features;

import com.analysis.SalaryStratos.dataStructures.array.SortedArray;
import com.analysis.SalaryStratos.models.WordFrequency;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SearchFrequency {

    //Uses LRUCache class
    private int CACHESIZE = 50;
    private int RETURNARRAYSIZE = 10;
    private final LRUCache<String, Integer> searchFrequencyMap = new LRUCache<>(CACHESIZE); // Adjust the cache size as needed

    public SortedArray<WordFrequency> displaySearchFrequencies() {
        SortedArray<WordFrequency> recentEntries = new SortedArray<>(Comparator.comparingInt(WordFrequency::getFrequency));
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(searchFrequencyMap.entrySet());

        // Get the last 10 entries
        int startIndex = Math.max(0, entries.size() - RETURNARRAYSIZE);
        for (int i = startIndex; i < entries.size(); i++) {
            Map.Entry<String, Integer> entry = entries.get(i);
            WordFrequency word= new WordFrequency(entry.getValue(), entry.getKey());
            recentEntries.insert(word);
        }

        return recentEntries;
    }

    //Called to update the LinkedHashMap of search term and frequency
    public void updateSearchFrequency(String[] searchTerms) {
        for(String term: searchTerms) {
            if(Objects.nonNull(term) && !term.isBlank()) {
                searchFrequencyMap.put(term.toLowerCase(), searchFrequencyMap.getOrDefault(term, 0) + 1);
            }

        }
    }
}
