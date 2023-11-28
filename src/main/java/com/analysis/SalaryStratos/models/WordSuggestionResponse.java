package com.analysis.SalaryStratos.models;


import java.util.List;

public class WordSuggestionResponse {
    List<SuggestionModel> wordSuggestions;

    boolean isResponseValid;

    public boolean isResponseValid() {
        return isResponseValid;
    }

    public void setResponseValid(boolean responseValid) {
        isResponseValid = responseValid;
    }

    public List<SuggestionModel> getWordSuggestions() {
        return wordSuggestions;
    }

    public void setWordSuggestions(List<SuggestionModel> wordSuggestions) {
        this.wordSuggestions = wordSuggestions;
    }
}
