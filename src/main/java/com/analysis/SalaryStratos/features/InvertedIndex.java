package com.analysis.SalaryStratos.features;

import com.analysis.SalaryStratos.models.Job;

import java.io.*;
import java.util.*;

class Index {
    Map<String, String> sources;
    HashMap<String, HashSet<String>> index;

    Index() {
        sources = new HashMap<>();
        index = new HashMap<>();
    }

    public void buildIndex(){
        List<Job> jobsList = FetchAndUpdateData.readJsonData();
        int i = 0;
        for (Job job : jobsList) {
            sources.put(job.getId(), job.getJobTitle());
            String jobDescription = job.getJobDescription().replaceAll("\\\\n|\\n", " ");
            //Matches one or more characters that are not alphabetic
            String[] words = jobDescription.trim().split("\\P{Alpha}+");
            System.out.println(jobDescription);
            for(String word : words){
                System.out.println(word);
            }

            System.out.println("--------------------------");
            for (String word : words) {
                word = word.toLowerCase();
                if (!index.containsKey(word))
                    index.put(word, new HashSet<>());
                index.get(word).add(job.getId());
            }
        }

    }

    public void find(String phrase) {
        //Matches one or more characters that are not alphabetic
        String[] words = phrase.trim().split("\\P{Alpha}+");
        HashSet<String> res = new HashSet<>(index.get(words[0].toLowerCase()));

        for (String word : words) {
            res.retainAll(index.get(word.toLowerCase()));
        }

        if (res.size() == 0) {
            System.out.println("Not found");
            return;
        }

        System.out.println("Found in: ");
        for (String jobId : res) {
            System.out.println("\t" + sources.get(jobId));
        }
    }

    public List<String> findAndReturnIds(String phrase) {
        //Matches one or more characters that are not alphabetic
        String[] words = phrase.trim().split("\\P{Alpha}+");
        HashSet<String> res = new HashSet<>(index.get(words[0].toLowerCase()));

        for (String word : words) {
            res.retainAll(index.get(word.toLowerCase()));
        }
        return res.stream().toList();
    }
}

public class InvertedIndex {
    public static void main(String args[]) throws IOException{
        Index index = new Index();
        index.buildIndex();

        System.out.println("Print search phrase: ");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        try {
            String phrase1 = in.readLine();
            String phrase = phrase1.toLowerCase();
            index.find(phrase);
            System.out.println(index.findAndReturnIds(phrase));
        } catch (IOException e) {
            System.out.println("Error reading input: " + e.getMessage());
        }
    }

    public static List<String> documentIdsWherePhraseFound(String phrase){
        Index index = new Index();
        index.buildIndex();

        return index.findAndReturnIds(phrase);
    }
}
