package org.example.featureExtractor;

import org.example.Article;

import java.util.*;

public class MostFrequentCapitalized {
    public static String mostFrequentCapitalizedWord(Article article) {
        String text = article.getText();
        if (text == null || text.isBlank()) {
            return "Brak słów";
        }

        String[] sentences = text.split("(?<=[.!?])\\s+");

        Map<String, Integer> wordCount = new HashMap<>();

        for (String sentence : sentences) {
            // Dzielimy zdanie na słowa
            String[] words = sentence.split("\\s+");

            // Pomijamy pierwsze słowo w każdym zdaniu
            for (int i = 1; i < words.length; i++) {
                String word = words[i].replaceAll("[^\\p{L}]", "");

                if (!word.isEmpty() && Character.isUpperCase(word.charAt(0))) {
                    wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
                }
            }
        }

        return wordCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Brak słów zaczynających się od dużej litery");
    }
}
