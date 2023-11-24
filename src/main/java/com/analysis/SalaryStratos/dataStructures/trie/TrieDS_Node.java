package com.analysis.SalaryStratos.dataStructures.trie;

public class TrieDS_Node {
    TrieDS_Node[] nodes = new TrieDS_Node[26];
    int frequency;
    boolean isEnd;

    public int getFrequency() {
        return frequency;
    }

    public void incrementFrequency() {
        frequency++;
    }

    public TrieDS_Node[] getDescendant() {
        return nodes;
    }
}
