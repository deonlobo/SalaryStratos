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

    // Search for an element using binary search
//    public int search(int key) {
//        T dummyElement = createDummyElement(key);
//        int index = Collections.binarySearch(dataList, dummyElement, comparator);
//        return index >= 0 ? index : -1;
//    }
//
//    // Create a dummy element with the specified key for searching
//    private T createDummyElement(int key) {
//        // Implement this method based on the structure of your objects
//        // This is just a placeholder; replace it with your actual object creation logic
//        return null;
//    }

    // Print the elements of the data structure
    public void printData() {
        for (T element : dataList) {
            System.out.println(element.toString());
        }
    }
}
//
//public class Main {
//    public static void main(String[] args) {
//        CustomDataStructure<MyObject> customDataStructure = new CustomDataStructure<>(Comparator.comparingInt(MyObject::getIntValue));
//
//        // Insert elements
//        customDataStructure.insert(new MyObject(5));
//        customDataStructure.insert(new MyObject(3));
//        customDataStructure.insert(new MyObject(8));
//        customDataStructure.insert(new MyObject(1));
//
//        // Print the sorted elements
//        customDataStructure.printData();
//
//        // Search for an element
//        int searchKey = 3;
//        int searchResult = customDataStructure.search(searchKey);
//        System.out.println("\nSearch result for key " + searchKey + ": Index " + searchResult);
//    }
//}
