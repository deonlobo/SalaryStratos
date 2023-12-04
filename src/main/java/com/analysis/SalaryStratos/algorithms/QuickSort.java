package com.analysis.SalaryStratos.algorithms;

import com.analysis.SalaryStratos.models.Job;

import java.util.List;

public class QuickSort {

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
