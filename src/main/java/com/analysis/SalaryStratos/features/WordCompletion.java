package com.analysis.SalaryStratos.features;

import com.analysis.SalaryStratos.dataStructures.array.SortedArray;
import com.analysis.SalaryStratos.models.SuggestionModel;
import com.analysis.SalaryStratos.models.WordFrequency;
import com.analysis.SalaryStratos.models.WordSuggestionResponse;
import com.analysis.SalaryStratos.services.JobDataTrie;

import java.util.ArrayList;
import java.util.List;

public class WordCompletion {

    public static WordSuggestionResponse getWordSuggestions(List<String> validatedSearchTerms, JobDataTrie jobData, int suggestionCount) {
        WordSuggestionResponse suggestions = new WordSuggestionResponse();

        if (validatedSearchTerms.isEmpty()) {
            System.out.println("IsEmpty");
            suggestions.setResponseValid(false);
        } else {
            List<SuggestionModel> suggestedWordsList = new ArrayList<>();
            suggestions.setResponseValid(true);
            for (String searchPrefix: validatedSearchTerms) {
                System.out.println(searchPrefix);
                SuggestionModel model = new SuggestionModel();
                model.setWord(searchPrefix);
                SortedArray<WordFrequency> arr = jobData.suggestWordsBasedOnPrefix(searchPrefix);
                System.out.println(Math.min(suggestionCount-1, suggestedWordsList.size()));
                ArrayList<WordFrequency> newList = new ArrayList<>(arr.getDataList().subList(0, Math.min(suggestionCount, arr.getDataList().size())));
                System.out.println(newList.size());
                arr.setDataList(newList);
                model.setSuggestedWords(arr);

                suggestedWordsList.add(model);
            }

            suggestions.setWordSuggestions(suggestedWordsList);
        }
        return suggestions;
    }
}
