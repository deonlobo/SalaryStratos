//package com.analysis.SalaryStratos.features;
//import com.analysis.SalaryStratos.algorithms.EditDistanceAlgo;
//import com.analysis.SalaryStratos.dataStructures.trie.TrieDS;
//import com.analysis.SalaryStratos.dataStructures.trie.TrieDS_Node;
//
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.*;
//
//public class SpellChecker {
//    TrieDS trie = new TrieDS();
//    Map<String, Integer> dict = new HashMap<>();
//    final static List<String> invalid = Arrays.asList("lol", "abcdefghijklmnopqrstuvwxyz");
//
//    String dictionaryFilePath;
//
//    SpellChecker(String dictionaryFilePath) {
//        System.out.println(invalid);
//        this.dictionaryFilePath = dictionaryFilePath;
//    }
//
//    public void initializeDictionary() {
//        try {
//            FileReader fr = new FileReader(dictionaryFilePath);
//            BufferedReader br = new BufferedReader(fr);
//            String line = null;
//            while ((line = br.readLine()) != null) {
//                String word = line.toLowerCase();
//                if (!line.contains(" ")) {
//                    dict.put(word, dict.getOrDefault(word, 0)+1);
//                    trie.insertIntoTrie(word, null);
//                } else {
//                    String[] strs= line.split("\\s");
//                    for(String str: strs) {
//                        dict.put(str, dict.getOrDefault(str, 0)+1);
//                        trie.add(str);
//                    }
//                }
//            }
//            fr.close();
//            br.close();
//        } catch (IOException e) {
//            System.err.println(e);
//        }
//    }
//
//    public TreeMap<Integer, TreeMap<Integer, TreeSet<String>>> suggestSimilarWord(String inputWord) {
//        if (inputWord.isEmpty() || invalid.contains(inputWord.toLowerCase()))
//            return null;
//        String s = inputWord.toLowerCase();
//        TreeMap<Integer, TreeMap<Integer, TreeSet<String>>> res;
//        TreeMap<Integer, TreeMap<Integer, TreeSet<String>>> map = new TreeMap<>();
//        TrieDS_Node node = trie.find(s);
//        System.out.println(node);
//        if(node == null) {
//            //System.out.println("\nnot find:" +s);
//            for (String w: dict.keySet()) {
//                int dist = EditDistanceAlgo.calculateLevenshteinDistance(w, s);
//                TreeMap<Integer, TreeSet<String>> similarWords = map.getOrDefault(dist, new TreeMap<>());
//                int wordFrequency = dict.get(w);
//                TreeSet<String> set = similarWords.getOrDefault(wordFrequency, new TreeSet<>());
//                set.add(w);
//                similarWords.put(wordFrequency, set);
//                map.put(dist, similarWords);
//            }
//            res= map;
//            //System.out.println(res+ " "+dict.get(res));
//        } else {
//            //System.out.println("\nfind:" +s+" "+dict.get(s));
//            res = null;
//        }
//        return res;
//    }
//
//
//    public static void main(String[] args) throws IOException {
//        String inputWord = "enginee";
//
//        SpellChecker checker = new SpellChecker("src/main/resources/dictionaryOfWords.txt");
//
//        checker.initializeDictionary();
//        TreeMap<Integer, TreeMap<Integer, TreeSet<String>>> suggestion = checker.suggestSimilarWord(inputWord);
//        if (suggestion == null) {
//            System.out.println("correct word");
//        }
//
//        System.out.println("Suggestion is: " + suggestion);
//    }
//
//}
