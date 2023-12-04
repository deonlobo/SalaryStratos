package com.analysis.SalaryStratos.features;

import com.analysis.SalaryStratos.algorithms.MergeSort;
import com.analysis.SalaryStratos.algorithms.QuickSort;
import com.analysis.SalaryStratos.dataStructures.trie.TrieDS;
import com.analysis.SalaryStratos.dataStructures.trie.TrieNode;
import com.analysis.SalaryStratos.models.CompareRunTimesData;
import com.analysis.SalaryStratos.models.Job;
import com.analysis.SalaryStratos.services.JobDataTrie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.*;

@Service
public class CompareRunTimes {
    @Autowired
    private  final PageRanking pageRanking;

    public CompareRunTimes(PageRanking pageRanking) {
        this.pageRanking = pageRanking;
    }

    public CompareRunTimesData compareRunTimeForSortingAlgorithms(String[] searchTerms, TrieDS trie, JobDataTrie jobData) throws FileNotFoundException, InterruptedException {
        long startTime;
        long endTime;
        CompareRunTimesData compareRunTimesData = new CompareRunTimesData();

        //Perform merge sort
        startTime = System.nanoTime();
        MergeSort.mergeSort(searchInvertedIndexedData(searchTerms, trie, jobData));
        endTime = System.nanoTime();
        compareRunTimesData.setMergeSort(endTime-startTime);

        //Perform  binary Search
        startTime = System.nanoTime();
        pageRanking.searchInvertedIndexedData(searchTerms, jobData.getInitTrie(), jobData );
        endTime = System.nanoTime();
        compareRunTimesData.setBinarySearch(endTime-startTime);

        // Perform quicksort
        startTime = System.nanoTime();
        List<Job> jobs = searchInvertedIndexedData(searchTerms, trie, jobData);
        QuickSort.quickSort(jobs, 0, jobs.size() - 1);
        endTime = System.nanoTime();
        compareRunTimesData.setQuickSort(endTime-startTime);


        return compareRunTimesData;
    }

    public List<Job> searchInvertedIndexedData(String[] searchTerms, TrieDS trie, JobDataTrie jobData) throws FileNotFoundException, InterruptedException {
        ArrayList<Job> jobs= jobData.getJobsDataFromJson();

        List<Job> unSortedJobs = new ArrayList<>();
        for (String term: searchTerms) {
            TrieNode node = trie.searchInTrie(term.toLowerCase());
            Set<String> jobIds = node.getJobIds();
            TreeMap<String, Integer> wordFrequency = node.getWordFrequency();

            for (String jobId: jobIds) {
                Job jobById = jobs.stream()
                        .filter(job -> Objects.equals(job.getId(), jobId))
                        .findFirst()
                        .orElse(null);

                if(jobById != null) {
                    if ( jobById.getWordFrequency() == 0) {
                        jobById.setWord(term);
                        jobById.setWordFrequency(wordFrequency.get(jobId));

                        unSortedJobs.add(jobById);
                    } else {
                        unSortedJobs.remove(jobById);
                        jobById.setWord(jobById.getWord() + ", " + term);
                        jobById.setWordFrequency(jobById.getWordFrequency() + wordFrequency.get(jobId));
                        unSortedJobs.add(jobById);

                    }
                }
            }
        }

        return unSortedJobs;
    }

}
