package com.analysis.SalaryStratos.features;
import com.analysis.SalaryStratos.algorithms.EditDistanceAlgo;
import com.analysis.SalaryStratos.dataStructures.trie.TrieDS;
import com.analysis.SalaryStratos.dataStructures.trie.TrieNode;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Service
public class SpellChecker {
    // List of invalid words to be excluded from suggestions.
    private final List<String> invalid = Arrays.asList("lol", "abcdefghijklmnopqrstuvwxyz");

    // List of common English stop words
    Set<String> stopWords = new HashSet<>(Arrays.asList(
            "why","wouldn","very","to","this","they","than","t","s","shouldn","shan","same","re","own","only","or","on","off","no","nor","not","other","our","ours","ourselves","out","over","of","now","own","o","once","on","off","no","nor","not","only","or","other","our","ours","ourselves","out","over","of","now","m","mightn","me","myself","more","most","mustn","my","much","ma","many","ll","just","its","itself","is","it","into","in","i","ii","if","i","ii","if","into","in","is","it","itself","its","just","ll","many","much","my","myself","me","mightn","m","most","more","mustn","needn","near","never","nevertheless","how","however","himself","him","his","hers","herself","her","here","hence","have","has","had","hadn","having","haven","hasn","he","hence","here","her","hers","herself","him","his","himself","how","however","haven","having","had","hadn","hasn","has","have","hence","her","hers","herself","him","his","himself","how","however","were","was","wasn","us","up","upon","under","unless","until","unto","through","throughout","thru","thus","thence","thereupon","therefore","therein","thereafter","thereby","then","this","those","thou","though","thru","though","those","this","then ","thereby","thereafter","therein","therefore","thereupon","thence","thus","thru","unto","until","unless","under","upon","up","us","was","wasn","were","what","whatever","whence","whenever","whereupon","wherein","whereby","whereafter","whereas","wherever","where","whither","whoever","whole","whom","whomever","whose","who","why","with","within","without","wherewith","which","whichever","while","when","whenever","whence","where","whereafter","whereas","whereby","wherein","whereupon","wherever","wherever","whereupon","wherein","whereby","whereafter","whereas","wherever","where","whenever","whence","when","while","whichever","which","wherewith","without","within","with","why","who","whose","whomever","whom","whole","whoever","whither","while","when","whenever","whence","where","whereafter","whereas","whereby","wherein","whereupon","wherever"
    ));
    // Initialize the spell checker
    public void initializeSpellChecker(TrieDS trie) {
        Map<String, Integer> dict = trie.getDict();
        try {
            String pathToDictionary = "src/main/resources/dictionaryOfWords.txt";
            FileReader fr = new FileReader(pathToDictionary);
            BufferedReader br = new BufferedReader(fr);
            String line = null;
            while ((line = br.readLine()) != null) {
                String word = line.toLowerCase();
                if (!stopWords.contains(word.toLowerCase())) {
                    if (!line.contains(" ")) {
                        dict.put(word, dict.getOrDefault(word, 0) + 1);
                        trie.insertIntoTrie(word, "fromCorpus");
                    } else {
                        String[] strs = line.split("\\s");
                        for (String str : strs) {
                            dict.put(str, dict.getOrDefault(word, 0) + 1);
                            trie.insertIntoTrie(str, "fromCorpus");
                        }
                    }
                }
            }
            fr.close();
            br.close();
        } catch (IOException e) {
            System.err.println(e);
        }

        System.out.println("Spell Checker Initialized");
    }

    // Generate a map of suggested similar words based on edit distance.
    public TreeMap<Integer, TreeMap<Integer, TreeSet<String>>> suggestSimilarWord(String inputWord, TrieDS trie, int editDistanceLength) {
        Map<String, Integer> dict = trie.getDict();
        if (inputWord.isEmpty() || invalid.contains(inputWord.toLowerCase()))
            return null;
        String s = inputWord.toLowerCase();
        TreeMap<Integer, TreeMap<Integer, TreeSet<String>>> map = new TreeMap<>();
        TrieNode node = trie.searchInTrie(s);
        // If the input word is not present in the trie, find similar words.
        if(node == null) {
            for (String w: dict.keySet()) {
                int dist = EditDistanceAlgo.calculateEditDistance(w, s);
                if(dist <= editDistanceLength) {
                    // Build the map with edit distance, word frequency, and the set of similar words.
                    TreeMap<Integer, TreeSet<String>> similarWords = map.getOrDefault(dist, new TreeMap<>());
                    int wordFrequency = dict.get(w);
                    TreeSet<String> set = similarWords.getOrDefault(wordFrequency, new TreeSet<>());
                    set.add(w);
                    similarWords.put(wordFrequency, set);
                    map.put(dist, similarWords);
                }
            }
            return map;
        } else {
            return null;
        }
    }

}
