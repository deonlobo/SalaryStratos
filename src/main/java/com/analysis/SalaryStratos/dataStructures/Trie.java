package com.analysis.SalaryStratos.dataStructures;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class TrieNode {
    public Map<Character, TrieNode> children = new HashMap<>();
    public boolean isEndOfWord;
}

public class Trie {
    private final TrieNode root = new TrieNode();

    public void insert(String word) {
        TrieNode node = root;
        for (char ch : word.toCharArray()) {
            node.children.computeIfAbsent(ch, k -> new TrieNode());
            node = node.children.get(ch);
        }
        node.isEndOfWord = true;
    }

    public boolean search(String word) {
        TrieNode node = root;
        for (char ch : word.toCharArray()) {
            if (!node.children.containsKey(ch)) {
                return false;
            }
            node = node.children.get(ch);
        }
        return node.isEndOfWord;
    }
}

//public class SpellChecker {
//    private Trie trie;
//
//    public SpellChecker(Trie trie) {
//        this.trie = trie;
//    }
//
//    // Check if a word is spelled correctly
//    public boolean isSpelledCorrectly(String word) {
//        return trie.search(word);
//    }
//
////    // Get suggestions for a misspelled word
////    public List<String> getSuggestions(String misspelledWord) {
////        // Implementation using Levenshtein distance as before
////        // ...
////    }
//
//    public static void main(String[] args) {
//        // Example usage
//        Trie trie = new Trie();
//        trie.insert("hello");
//        trie.insert("world");
//        trie.insert("spell");
//        // Add more words to the trie
//
//        SpellChecker spellChecker = new SpellChecker(trie);
//
//        String wordToCheck = "wold";
//        if (spellChecker.isSpelledCorrectly(wordToCheck)) {
//            System.out.println(wordToCheck + " is spelled correctly.");
//        } else {
////            System.out.println(wordToCheck + " is misspelled. Suggestions: " + spellChecker.getSuggestions(wordToCheck));
//        }
//    }
//}