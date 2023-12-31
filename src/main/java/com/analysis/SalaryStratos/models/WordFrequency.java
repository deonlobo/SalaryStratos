package com.analysis.SalaryStratos.models;

public class WordFrequency {
    // Frequency of the word in a corpus.
    private int frequency;

    // The word itself.
    private String word;

    /**
     * Constructs a WordFrequency object with the given frequency and word.
     *
     * @param frequency The frequency of the word in a corpus.
     * @param word      The word.
     */
    public WordFrequency(int frequency, String word) {
        this.frequency = frequency;
        this.word = word;
    }

    /**
     * Gets the frequency of the word.
     *
     * @return The frequency of the word.
     */
    public int getFrequency() {
        return frequency;
    }

    /**
     * Gets the word.
     *
     * @return The word.
     */
    public String getWord() {
        return word;
    }
}
