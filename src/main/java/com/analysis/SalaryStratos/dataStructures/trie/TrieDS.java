package com.analysis.SalaryStratos.dataStructures.trie;

public class TrieDS {
    TrieDS_Node root = new TrieDS_Node();
    int myHashCode = 0;
    
    public void add(String word) {
        TrieDS_Node p = root;
        for (char c : word.toCharArray()) {
            int numericValue = c - 'a';
            if (p.nodes[numericValue] == null)
                p.nodes[numericValue] = new TrieDS_Node();
            p = p.nodes[numericValue];
        }
        myHashCode +=word.hashCode();
        p.incrementFrequency();
        p.isEnd = true;
    }

    public TrieDS_Node find(String word) {
        TrieDS_Node p = root;
        for (char c : word.toCharArray()) {
            if (p.nodes[c-'a'] == null)
                return null;
            p = p.nodes[c-'a'];
        }
        if( p != null && p.isEnd)
            return p;
        return null;
    }

    public int getNodeCount() {
        return countNodesInTrie(root);
    }

    private int countNodesInTrie(TrieDS_Node node)  {
        if (node == null)
            return 0;
        int count = 0;
        for (int i = 0; i < 26; i++) {
            if (node.nodes[i] != null)
                count += countNodesInTrie(node.nodes[i]);
        }
        return (1 + count);
    }

    public int getWordCount() {
        return wordCount(root);
    }

    private int wordCount(TrieDS_Node root)  {
        int result = 0;
        if (root.isEnd)
            result++;
        for (int i = 0; i < 26; i++)
            if (root.nodes[i] != null )
                result += wordCount(root.nodes[i]);
        return result;
    }

    @Override
    public String toString() {
        char[] wordArray = new char[50];
        StringBuilder sb = new StringBuilder();
        printAllWords(root, wordArray, 0, sb);
        if(sb.toString().isEmpty())
            return "";
        return sb.substring(1);
    }

    private void printAllWords(TrieDS_Node root, char[] wordArray, int pos, StringBuilder sb) {
        if(root == null)
            return;
        if(root.isEnd)  {
            sb.append("\n");
            for(int i=0; i<pos; i++) {
                sb.append(wordArray[i]);
            }
        }
        for(int i=0; i<26; i++) {
            if(root.nodes[i] != null)  {
                wordArray[pos] = (char)(i+'a');
                printAllWords(root.nodes[i], wordArray, pos+1, sb);
            }
        }
    }

    @Override
    public int hashCode(){
        return myHashCode;
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof TrieDS s) {
            if(this.getNodeCount() != s.getNodeCount())
                return false;
            if(this.getWordCount() != s.getWordCount())
                return false;
            return compareTrie(this, s);
        } else {
            return false;
        }
    }

    private boolean compareTrie(TrieDS p, TrieDS q) {
        String s1 = p.toString();
        String s2 = q.toString();
        if(s1.isEmpty() && s2.isEmpty())
            return true;
        String[] strs1 = s1.split("\n");
        String[] strs2= s2.split("\n");
        if(strs1.length!= strs2.length)
            return false;
        for(String s: strs1) {
            TrieDS_Node node1 = p.find(s);
            TrieDS_Node node2 = q.find(s);
            if(node1.getFrequency()!=node2.getFrequency())
                return false;
        }
        return true;
    }
}