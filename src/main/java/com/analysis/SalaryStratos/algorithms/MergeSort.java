package com.analysis.SalaryStratos.algorithms;

import com.analysis.SalaryStratos.models.Job;

import java.util.ArrayList;
import java.util.List;

public class MergeSort {

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
}
