package com.analysis.SalaryStratos.dataStructures.trie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrieDS {
    TrieNode root;

    public TrieDS() {
        this.root = new TrieNode();
    }

    public void insertIntoTrie(String word, String jobId) {
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

        return node;
    }

    public List<String> searchInTrieWithPrefix(String prefix) {
        TrieNode node = root;
        for (char c : prefix.toCharArray()) {
            if (!node.children.containsKey(c)) {
                return Collections.emptyList();
            }
            node = node.children.get(c);
        }
        System.out.println(node.isEndOfWord);
        System.out.println(node.children.keySet());
        if(node.children == null) {
            return new ArrayList<>();
        }
        List<String> suggestions = new ArrayList<>();
        collectWords(node, prefix, suggestions);
        return suggestions;
    }

    // Recursive function to collect words from a trie node
    private void collectWords(TrieNode node, String currentPrefix, List<String> suggestions) {
        if (node.isEndOfWord) {
            suggestions.add(currentPrefix);
        }
        for (char c : node.children.keySet()) {
            collectWords(node.children.get(c), currentPrefix + c, suggestions);
        }
    }
}