package com.analysis.SalaryStratos.dataStructures.array;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortedArray<T> {
    private List<T> dataList;
    private final Comparator<T> comparator;

    public SortedArray(Comparator<T> comparator) {
        this.dataList = new ArrayList<>();
        this.comparator = comparator;
    }

    public List<T> getDataList() {
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

    public void delete(T element) {
        int index = dataList.indexOf(element);
        if(index>-1) {
            dataList.remove(index);
        }
    }

    public  void setDataList(List<T> lst) {
        this.dataList = lst;
    }
}