package com.analysis.SalaryStratos.dataStructures.trie;

import java.util.*;

public class TrieNode {
    Map<Character, TrieNode> children;
    Set<String> jobIds;
    TreeMap<String, Integer> wordFrequency;
    boolean isEndOfWord;

    public TrieNode() {
        this.children = new HashMap<>();
        this.jobIds = new HashSet<>();
        this.wordFrequency = new TreeMap<>();
        this.isEndOfWord = false;
    }

    public Map<Character, TrieNode> getChildren() {
        return children;
    }

    public void setChildren(Map<Character, TrieNode> children) {
        this.children = children;
    }

    public Set<String> getJobIds() {
        return jobIds;
    }

    public void setJobIds(Set<String> jobIds) {
        this.jobIds = jobIds;
    }

    public TreeMap<String, Integer> getWordFrequency() {
        return wordFrequency;
    }

    public void setWordFrequency(TreeMap<String, Integer> wordFrequency) {
        this.wordFrequency = wordFrequency;
    }
}
