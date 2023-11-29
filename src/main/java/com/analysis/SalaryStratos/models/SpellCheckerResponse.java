package com.analysis.SalaryStratos.models;

import java.util.TreeMap;
import java.util.TreeSet;

public class SpellCheckerResponse {
    String word;

    TreeMap<Integer, TreeMap<Integer, TreeSet<String>>> correctWords;

    public SpellCheckerResponse(String word, TreeMap<Integer, TreeMap<Integer, TreeSet<String>>> correctWords) {
        this.word = word;
        this.correctWords = correctWords;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public TreeMap<Integer, TreeMap<Integer, TreeSet<String>>> getCorrectWords() {
        return correctWords;
    }

    public void setCorrectWords(TreeMap<Integer, TreeMap<Integer, TreeSet<String>>> correctWords) {
        this.correctWords = correctWords;
    }
}
