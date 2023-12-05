package com.analysis.SalaryStratos.models;

import com.analysis.SalaryStratos.dataStructures.array.SortedArray;

public class SuggestionModel {
    String word;
    SortedArray<WordFrequency> suggestedWords;



    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public SortedArray<WordFrequency> getSuggestedWords() {
        return suggestedWords;
    }

    public void setSuggestedWords(SortedArray<WordFrequency> suggestedWords) {
        this.suggestedWords = suggestedWords;
    }
}
