package com.analysis.SalaryStratos.algorithms;

import java.util.ArrayList;
import java.util.List;

public class EditDistanceAlgo {

    public static int calculateLevenshteinDistance(String word1, String word2) {
        int[][] dp = new int[word1.length() + 1][word2.length() + 1];

        for (int i = 0; i <= word1.length(); i++) {
            for (int j = 0; j <= word2.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    int cost = (word1.charAt(i - 1) == word2.charAt(j - 1)) ? 0 : 1;
                    dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1), dp[i - 1][j - 1] + cost);
                }
            }
        }

        return dp[word1.length()][word2.length()];
    }

    public static List<String> getClosestWords(String word, List<String> dictionary, int maxDistance) {
        List<String> closestWords = new ArrayList<>();

        for (String dictWord : dictionary) {
            int distance = calculateLevenshteinDistance(word, dictWord);
            if (distance <= maxDistance) {
                closestWords.add(dictWord);
            }
        }

        return closestWords;
    }

}
