package com.analysis.SalaryStratos.algorithms;

import com.analysis.SalaryStratos.models.Job;

import java.util.ArrayList;
import java.util.List;

public class MergeSort {

    public  static List<Job> mergeSortAlgo(List<Job> jobList) {
        if ( jobList.size()  <=  1 ) {
            return jobList;
        }

        int middleEle = jobList.size() / 2;
        List<Job> leftX = jobList.subList(0, middleEle);
        List<Job> rightX = jobList.subList(middleEle, jobList.size());

        leftX = mergeSortAlgo(leftX);
        rightX = mergeSortAlgo(rightX);

        return merge(leftX, rightX);
    }

    public static List<Job> merge(List<Job> leftX, List<Job> rightX) {
        List<Job> resultX = new ArrayList<>();
        int leftIndexX = 0;
        int rightIndexX = 0;

        while (leftIndexX < leftX.size() && rightIndexX < rightX.size()) {
            if (leftX.get(leftIndexX).getWordFrequency() >= rightX.get(rightIndexX).getWordFrequency()) {
                resultX.add(leftX.get(leftIndexX));
                leftIndexX++;
            } else {
                resultX.add(rightX.get(rightIndexX));
                rightIndexX++;
            }
        }

        resultX.addAll(leftX.subList(leftIndexX, leftX.size()));
        resultX.addAll(rightX.subList(rightIndexX, rightX.size()));

        return resultX;
    }
}
