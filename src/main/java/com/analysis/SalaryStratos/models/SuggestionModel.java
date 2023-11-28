package com.analysis.SalaryStratos.models;

import java.util.List;

public class SuggestionModel {
    String word;
    List<String> suggestedWords;



    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public List<String> getSuggestedWords() {
        return suggestedWords;
    }

    public void setSuggestedWords(List<String> suggestedWords) {
        this.suggestedWords = suggestedWords;
    }
}
