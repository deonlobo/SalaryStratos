package com.analysis.SalaryStratos.services;


import com.analysis.SalaryStratos.dataStructures.array.SortedArray;
import com.analysis.SalaryStratos.dataStructures.trie.TrieDS;
import com.analysis.SalaryStratos.dataStructures.trie.WordFrequency;
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
import java.util.List;
import java.util.Map;
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
    private final Gson jsonHandler = new Gson();
    private ArrayList<Job> jobsList = new ArrayList<>();
    private final TrieDS jobsDataTrie = new TrieDS();
    String[] searchTermsList = new String[]{
            "Engineer", "Exec", "Senior", "Developer", "Finance", "Sys Admin", "JavaScript", "Backend", "Golang", "Cloud", "Medical", "Front End", "Full Stack", "Ops", "Design", "React", "InfoSec", "Marketing", "Mobile", "Content Writing", "SaaS", "Recruiter", "Full Time", "API", "Sales", "Ruby", "Education", "DevOps", "Stats", "Python", "Node", "English", "Non Tech", "Video", "Travel", "Quality Assurance", "Ecommerce", "Teaching", "Linux", "Java", "Crypto", "Junior", "Git", "Legal", "Android", "Accounting", "Admin", "Microsoft", "Excel", "PHP"
    };

    public JobDataTrie(SimplyHiredScraper simplyHiredScraper, RemoteOk remoteOk, GlassDoorScraper glassDoorScraper) {
        this.simplyHiredScraper = simplyHiredScraper;
        this.remoteOk = remoteOk;
        this.glassDoorScraper = glassDoorScraper;
    }

    public void getJobsDataFromJson() throws FileNotFoundException, InterruptedException {
        Gson gson = new Gson();
        Jobs jobs = null;
        try {
            // Read the existing data from the file
            jobs = gson.fromJson(new FileReader("src/main/resources/database.json"), Jobs.class);
        } catch (FileNotFoundException e) {
            //File doesnt exist
            simplyHiredScraper.crawlWebPage(searchTermsList);
            remoteOk.crawlRemoteOk(searchTermsList);
            glassDoorScraper.crawlWebPage(searchTermsList);
            jobs = gson.fromJson(new FileReader("src/main/resources/database.json"), Jobs.class);
        }
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

   /* public static void main(String[] args) throws FileNotFoundException {
        JobDataTrie j = new JobDataTrie(simplyHiredScraper, remoteOk, glassDoorScraper);
        j.getJobsDataFromJson();
        j.initializeTrie();
    }*/

    public SortedArray<WordFrequency> suggestWordsBasedOnPrefix(String wordPrefix) {
        return jobsDataTrie.searchInTrieWithPrefix(wordPrefix);
    }

}
