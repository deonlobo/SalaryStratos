package com.analysis.SalaryStratos.features;

import com.analysis.SalaryStratos.dataStructures.array.SortedArray;
import com.analysis.SalaryStratos.models.SuggestionModel;
import com.analysis.SalaryStratos.models.WordFrequency;
import com.analysis.SalaryStratos.models.WordSuggestionResponse;
import com.analysis.SalaryStratos.services.JobDataTrie;

import java.util.ArrayList;
import java.util.List;

public class WordCompletion {

    // Get word suggestions based on a list of validated search terms, a trie, and a suggestion count.
    public static WordSuggestionResponse getWordSuggestions(List<String> validatedSearchTerms, JobDataTrie jobDataTrie, int suggestionCount) {
        WordSuggestionResponse suggestions = new WordSuggestionResponse();

        // Check if search terms are valid.
        if (validatedSearchTerms.isEmpty()) {
            System.out.println("Search Terms are Invalid");
            suggestions.setResponseValid(false);
        } else {
            List<SuggestionModel> suggestedWordsList = new ArrayList<>();
            boolean isValid = false;
            // Iterate through each validated search term to get word suggestions.
            for (String searchPrefix: validatedSearchTerms) {
                SuggestionModel model = new SuggestionModel();
                model.setWord(searchPrefix);
                // Get word suggestions based on the trie and search prefix.
                SortedArray<WordFrequency> arr = jobDataTrie.suggestWordsBasedOnPrefix(searchPrefix);

                // If suggestions are available, add them to the model.
                if(arr != null) {
                    ArrayList<WordFrequency> newList = new ArrayList<>(arr.getDataList().subList(0, Math.min(suggestionCount, arr.getDataList().size())));
                    arr.setDataList(newList);
                    model.setSuggestedWords(arr);
                    suggestedWordsList.add(model);
                    isValid = true;
                }
            }
            // Set the response validity and word suggestions in the response object.
            suggestions.setResponseValid(isValid);

            suggestions.setWordSuggestions(suggestedWordsList);
        }
        return suggestions;
    }
}
