package com.analysis.SalaryStratos.features;

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
public class CompareRumTimes {
    @Autowired
    private  final PageRanking pageRanking;

    public CompareRumTimes(PageRanking pageRanking) {
        this.pageRanking = pageRanking;
    }

    public CompareRunTimesData compareRunTimeForSortingAlgorithms(String[] searchTerms, TrieDS trie, JobDataTrie jobData) throws FileNotFoundException, InterruptedException {
        long startTime;
        long endTime;
        CompareRunTimesData compareRunTimesData = new CompareRunTimesData();

        // Perform merge sort
        startTime = System.currentTimeMillis();
        mergeSort(searchInvertedIndexedData(searchTerms, trie, jobData));
        endTime = System.currentTimeMillis();
        compareRunTimesData.setMergeSort(endTime-startTime);

        //Perform binary Search
        startTime = System.currentTimeMillis();
        pageRanking.searchInvertedIndexedData(searchTerms, jobData.getInitTrie(), jobData );
        endTime = System.currentTimeMillis();
        compareRunTimesData.setBinarySearch(endTime-startTime);

        // Perform quicksort
        startTime = System.currentTimeMillis();
        List<Job> jobs = searchInvertedIndexedData(searchTerms, trie, jobData);
        quickSort(jobs, 0, jobs.size() - 1);
        endTime = System.currentTimeMillis();
        compareRunTimesData.setQuickSort(endTime-startTime);


        return compareRunTimesData;
    }

    public List<Job> searchInvertedIndexedData(String[] searchTerms, TrieDS trie, JobDataTrie jobData) throws FileNotFoundException, InterruptedException {
        ArrayList<Job> jobs= jobData.getJobsDataFromJson();

        List<Job> sortedJobs = new ArrayList<>();
        for (String term: searchTerms) {
            System.out.println(term);
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

                        sortedJobs.add(jobById);
                    } else {
                        sortedJobs.remove(jobById);
                        jobById.setWord(jobById.getWord() + ", " + term);
                        jobById.setWordFrequency(jobById.getWordFrequency() + wordFrequency.get(jobId));
                        sortedJobs.add(jobById);

                    }



                }

            }
        }

        return sortedJobs;
    }

    public static List<Job> mergeSort(List<Job> jobList) {
        if (jobList.size() <= 1) {
            return jobList;
        }

        int middle = jobList.size() / 2;
        List<Job> left = jobList.subList(0, middle);
        List<Job> right = jobList.subList(middle, jobList.size());

        left = mergeSort(left);
        right = mergeSort(right);

        return merge(left, right);
    }

    public static List<Job> merge(List<Job> left, List<Job> right) {
        List<Job> result = new ArrayList<>();
        int leftIndex = 0;
        int rightIndex = 0;

        while (leftIndex < left.size() && rightIndex < right.size()) {
            if (left.get(leftIndex).getWordFrequency() >= right.get(rightIndex).getWordFrequency()) {
                result.add(left.get(leftIndex));
                leftIndex++;
            } else {
                result.add(right.get(rightIndex));
                rightIndex++;
            }
        }

        result.addAll(left.subList(leftIndex, left.size()));
        result.addAll(right.subList(rightIndex, right.size()));

        return result;
    }

    public static void quickSort(List<Job> jobList, int low, int high) {
        if (low < high) {
            int partitionIndex = partition(jobList, low, high);

            // Recursively sort elements before and after the partition index
            quickSort(jobList, low, partitionIndex - 1);
            quickSort(jobList, partitionIndex + 1, high);
        }
    }

    private static int partition(List<Job> jobList, int low, int high) {
        int pivot = jobList.get(high).getWordFrequency();
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (jobList.get(j).getWordFrequency() >= pivot) {
                i++;
                swap(jobList, i, j);
            }
        }

        swap(jobList, i + 1, high);
        return i + 1;
    }

    private static void swap(List<Job> jobList, int i, int j) {
        Job temp = jobList.get(i);
        jobList.set(i, jobList.get(j));
        jobList.set(j, temp);
    }
}
