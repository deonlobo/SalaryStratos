package com.analysis.SalaryStratos.features;

import com.analysis.SalaryStratos.dataStructures.trie.TrieDS;
import com.analysis.SalaryStratos.dataStructures.trie.TrieNode;
import com.analysis.SalaryStratos.models.Job;

import java.util.*;

public class InvertedIndexer {

    public void searchInvertedIndexedData(String searchTerm, TrieDS jobsDataTrie, ArrayList<Job> jobs) {
        TrieNode node = jobsDataTrie.searchInTrie(searchTerm.toLowerCase());
        Set<String> jobIds = node.getJobIds();
        TreeMap<String, Integer> wordFrequency = node.getWordFrequency();

        for (String jobId: jobIds) {
            Job jobById = jobs.stream()
                    .filter(job -> Objects.equals(job.getId(), jobId))
                    .findFirst()
                    .orElse(null);
            assert jobById != null;
            System.out.println("JobId: " + jobId);
            System.out.println("WordFrequency: " + wordFrequency.get(jobId));
            System.out.println("Description: " + jobById.getJobDescription());
        }
    }
}
