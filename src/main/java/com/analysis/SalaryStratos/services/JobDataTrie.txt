package com.analysis.SalaryStratos.services;


import com.analysis.SalaryStratos.dataStructures.array.SortedArray;
import com.analysis.SalaryStratos.dataStructures.trie.TrieDS;
import com.analysis.SalaryStratos.models.WordFrequency;
import com.analysis.SalaryStratos.features.scraper.GlassDoorScraper;
import com.analysis.SalaryStratos.features.scraper.RemoteOk;
import com.analysis.SalaryStratos.features.scraper.SimplyHiredScraper;
import com.analysis.SalaryStratos.models.Job;
import com.analysis.SalaryStratos.models.Jobs;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class JobDataTrie {
    @Autowired
    private final SimplyHiredScraper simplyHiredScraper;
    @Autowired
    private final RemoteOk remoteOk;
    @Autowired
    private final GlassDoorScraper glassDoorScraper;
    private TrieDS jobsDataTrie;
    String[] searchTermsList = new String[]{
            "Engineer", "Exec", "Senior", "Developer", "Finance", "Sys Admin", "JavaScript", "Backend", "Golang", "Cloud", "Front End"
    };

    // List of common English stop words
    Set<String> stopWords = new HashSet<>(Arrays.asList(
            "i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours", "yourself",
            "yourselves", "he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its", "itself",
            "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that", "these",
            "those", "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having", "do",
            "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while",
            "of", "at", "by", "for", "with", "about", "against", "between", "into", "through", "during", "before",
            "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off", "over", "under", "again",
            "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each",
            "few", "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than",
            "too", "very", "s", "t", "can", "will", "just", "don", "should", "now", "d", "ll", "m", "o", "re", "ve", "y", "ain", "aren", "couldn", "didn", "doesn", "hadn", "hasn", "haven", "isn", "ma", "mightn", "mustn", "needn", "shan", "shouldn", "wasn", "weren", "won", "wouldn","ii"
    ));

    public JobDataTrie(SimplyHiredScraper simplyHiredScraper, RemoteOk remoteOk, GlassDoorScraper glassDoorScraper) {
        this.simplyHiredScraper = simplyHiredScraper;
        this.remoteOk = remoteOk;
        this.glassDoorScraper = glassDoorScraper;
    }

    public ArrayList<Job> getJobsDataFromJson() throws FileNotFoundException, InterruptedException {

        Gson gson = new Gson();
        Jobs jobs = null;
        try {
            // Read the existing data from the file
            jobs = gson.fromJson(new FileReader("src/main/resources/database.json"), Jobs.class);
        } catch (FileNotFoundException e) {
            //File doesnt exist
            System.out.println("File does not exist re-crawl data for default search terms");
            simplyHiredScraper.crawlWebPage(searchTermsList);
            remoteOk.crawlWebPage(searchTermsList);
            glassDoorScraper.crawlWebPage(searchTermsList);
            jobs = gson.fromJson(new FileReader("src/main/resources/database.json"), Jobs.class);
        }

        System.out.println("Job data loaded from json");

        return (ArrayList<Job>) jobs.getJobs();
    }

    public TrieDS initializeTrie() throws FileNotFoundException, InterruptedException {
        this.jobsDataTrie = new TrieDS();
        ArrayList<Job> jobsList = getJobsDataFromJson();
        for (Job job: jobsList) {
            String id = job.getId();
            String description = job.getJobDescription().replaceAll("\\\\n|\\n", " ").replaceAll(",","").replaceAll("\\.","");

            String[] descriptionTokens = description.trim().split(" ");

            for(String token: descriptionTokens) {

                String pattern = "[^a-zA-Z]+";

                Pattern regex = Pattern.compile(pattern);

                Matcher matcher = regex.matcher(token);

                String processedString = matcher.replaceAll("").toLowerCase();

                if (!stopWords.contains(processedString) && !processedString.isBlank()) {
                    jobsDataTrie.insertIntoTrie(processedString, id);
                }
            }

        }
        System.out.println("Trie init with jobs");

        return jobsDataTrie;

    }

    public TrieDS getInitTrie() {
        return  jobsDataTrie;
    }

    public SortedArray<WordFrequency> suggestWordsBasedOnPrefix(String wordPrefix) {
        return jobsDataTrie.searchInTrieWithPrefix(wordPrefix);
    }

}
