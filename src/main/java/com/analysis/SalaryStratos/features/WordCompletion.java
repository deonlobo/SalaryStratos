package com.analysis.SalaryStratos.features;

import com.analysis.SalaryStratos.models.SuggestionModel;
import com.analysis.SalaryStratos.models.WordSuggestionResponse;

import java.util.ArrayList;
import java.util.List;

public class WordCompletion {

    public static getWordSuggestions(String[] searchTerm) {
        WordSuggestionResponse suggestons = new WordSuggestionResponse();

        if (validatedSearchTerms.isEmpty()) {
            System.out.println("IsEmpty");
            suggestons.setResponseValid(false);
        } else {
            List<SuggestionModel> suggestedWordsList = new ArrayList<>();
            suggestons.setResponseValid(true);
            for (String searchPrefix: validatedSearchTerms) {
                System.out.println(searchPrefix);
                SuggestionModel model = new SuggestionModel();
                model.setWord(searchPrefix);

                model.setSuggestedWords(jobData.suggestWordsBasedOnPrefix(searchPrefix));

                suggestedWordsList.add(model);
            }

            suggestons.setWordSuggestions(suggestedWordsList);
        }
        return suggestons;
    }
}
