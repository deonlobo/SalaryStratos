package com.analysis.SalaryStratos.services;


import com.analysis.SalaryStratos.dataStructures.trie.TrieDS;
import com.analysis.SalaryStratos.models.Job;
import com.analysis.SalaryStratos.models.Jobs;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class JobDataTrie {

    private final Gson jsonHandler = new Gson();
    private ArrayList<Job> jobsList = new ArrayList<>();
    private final TrieDS jobsDataTrie = new TrieDS();

    public void getJobsDataFromJson() throws FileNotFoundException {
        Jobs jobs = jsonHandler.fromJson(new FileReader("src/main/resources/database.json"), Jobs.class);
        jobsList = (ArrayList<Job>) jobs.getJobs();
        System.out.println("Job data loaded from json");
    }



    public void initializeTrie() {

        for (Job job: jobsList) {
            String id = job.getId();
            String description = job.getJobDescription();

            String[] descriptionTokens = description.split(" ");

            for(String token: descriptionTokens) {

                String pattern = "[^a-zA-Z]+";

                Pattern regex = Pattern.compile(pattern);

                Matcher matcher = regex.matcher(token);

                String processedString = matcher.replaceAll("").toLowerCase();
                jobsDataTrie.insertIntoTrie(processedString, id);
            }

        }
        System.out.println("Trie init with jobs");

    }

    public static void main(String[] args) throws FileNotFoundException {
        JobDataTrie j = new JobDataTrie();
        j.getJobsDataFromJson();
        j.initializeTrie();
    }

    public List<String> suggestWordsBasedOnPrefix(String wordPrefix) {
        return jobsDataTrie.searchInTrieWithPrefix(wordPrefix);
    }

}
