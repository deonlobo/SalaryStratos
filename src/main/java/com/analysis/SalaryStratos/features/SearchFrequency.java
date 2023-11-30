package com.analysis.SalaryStratos.features;

import com.analysis.SalaryStratos.dataStructures.array.SortedArray;
import com.analysis.SalaryStratos.models.WordFrequency;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SearchFrequency {

    //Uses LRUCache class
    private int CACHESIZE = 5;
    private int RETURNARRAYSIZE = 4;
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

        //sort the recent entries
        // Sort recent entries based on frequency in descending order
//        recentEntries = recentEntries.entrySet()
//                .stream()
//                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
//                .collect(Collectors.toMap(
//                        Map.Entry::getKey,
//                        Map.Entry::getValue,
//                        (e1, e2) -> e1,
//                        LinkedHashMap::new
//                ));

        return recentEntries;
    }

    public void updateSearchFrequency(String[] searchTerms) {
        for(String term: searchTerms) {
            searchFrequencyMap.put(term, searchFrequencyMap.getOrDefault(term, 0) + 1);

        }
    }
}
