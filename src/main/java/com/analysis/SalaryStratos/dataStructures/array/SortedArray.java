package com.analysis.SalaryStratos.dataStructures.array;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SortedArray<T> {
    private ArrayList<T> dataList;
    private Comparator<T> comparator;

    public SortedArray(Comparator<T> comparator) {
        this.dataList = new ArrayList<>();
        this.comparator = comparator;
    }

    public ArrayList<T> getDataList() {
        return dataList;
    }

    public Comparator<T> getComparator() {
        return comparator;
    }

    // Insert an element while keeping the list sorted
    public void insert(T element) {
        int index = Collections.binarySearch(dataList, element, comparator.reversed());
        if (index < 0) {
            index = -index - 1;  // Adjust the index for insertion
        }
        dataList.add(index, element);
    }

    public  void setDataList(ArrayList<T> lst) {
        this.dataList = lst;
    }
    public void printData() {
        for (T element : dataList) {
            System.out.println(element.toString());
        }
    }
}