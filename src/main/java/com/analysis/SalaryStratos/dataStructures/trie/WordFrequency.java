package com.analysis.SalaryStratos.dataStructures.trie;

public class WordFrequency {
    private int frequency;
    private String word;
    // ... other fields and methods

    public WordFrequency(int frequency, String word) {
        this.frequency = frequency;
        this.word = word;
    }

    public int getFrequency() {
        return frequency;
    }

    public String getWord() {
        return word;
    }
}