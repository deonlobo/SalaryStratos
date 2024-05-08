package com.analysis.SalaryStratos.algorithms;

import com.analysis.SalaryStratos.models.Job;

import java.util.List;

public class QuickSort {

    public  static  void  quickSortAlgo (List<Job> jobList, int lowX, int highX) {
        if (lowX < highX) {
            int partitionIndex = partitionList(jobList, lowX, highX);

            // Recursively sort elements before and after the partition index
            quickSortAlgo(jobList, lowX, partitionIndex - 1);
            quickSortAlgo(jobList, partitionIndex + 1, highX);
        }
    }

    private  static  int  partitionList(List<Job> jobList, int lowX, int highX) {
        int pivotEle = jobList.get(highX).getWordFrequency();
        int ii = lowX - 1;

        for (int jj = lowX; jj < highX; jj++) {
            if (jobList.get(jj).getWordFrequency() >= pivotEle) {
                ii++;
                swapInList(jobList, ii, jj);
            }
        }

        swapInList(jobList, ii + 1, highX);
        return ii + 1;
    }

    private static void swapInList(List<Job> jobList, int ii, int jj) {
        Job tempX = jobList.get(ii);
        jobList.set(ii, jobList.get(jj));
        jobList.set(jj, tempX);
    }
}
