package com.analysis.SalaryStratos.dataStructures.trie;

import com.analysis.SalaryStratos.dataStructures.array.SortedArray;
import com.analysis.SalaryStratos.models.WordFrequency;

import java.util.*;

public class TrieDS {
    TrieNode root;

    Map<String, Integer> dict = new HashMap<>();



    public TrieDS() {
        this.root = new TrieNode();
    }

    public Map<String, Integer> getDict() {
        return dict;
    }

    public void insertIntoTrie(String word, String jobId) {

        dict.put(word, dict.getOrDefault(word, 0) +1);
        TrieNode node = root;

        for (char c : word.toCharArray()) {
            node.children.putIfAbsent(c, new TrieNode());
            node = node.children.get(c);
        }

        node.isEndOfWord = true;

        // Add the job ID to the set for this word
        node.jobIds.add(jobId);

        // Increment the count for this word in the current job ID
        node.wordFrequency.put(jobId, node.wordFrequency.getOrDefault(jobId, 0) + 1);
    }

    public TrieNode searchInTrie(String word) {
        TrieNode node = root;

        for (char c : word.toCharArray()) {
            if (!node.children.containsKey(c)) {
                return null; // Word not found
            }
            node = node.children.get(c);
        }

        if(node.isEndOfWord) {
            return  node;
        } else {
            return null;
        }
    }

    public SortedArray<WordFrequency> searchInTrieWithPrefix(String prefix) {
        TrieNode node = root;
        for (char c : prefix.toCharArray()) {
            if (!node.children.containsKey(c)) {
                return null;
            }
            node = node.children.get(c);
        }
        System.out.println(node.isEndOfWord);
        System.out.println(node.children.keySet());
        if(node.children == null) {
            return null;
        }
        SortedArray<WordFrequency> suggestions = new SortedArray<>(Comparator.comparingInt(WordFrequency::getFrequency));
        collectWords(node, prefix, suggestions);
        return suggestions;
    }

    // Recursive function to collect words from a trie node
    private void collectWords(TrieNode node, String currentPrefix, SortedArray<WordFrequency> suggestions) {
        if (node.isEndOfWord) {
            int totalFrequency = 0;
            for (Integer count: node.getWordFrequency().values()){
                totalFrequency += count;
            }
            WordFrequency word = new WordFrequency(totalFrequency, currentPrefix);
            suggestions.insert(word);
        }
        for (char c : node.children.keySet()) {
            collectWords(node.children.get(c), currentPrefix + c, suggestions);
        }
    }
}