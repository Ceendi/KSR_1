package org.example.featureExtractor;

import org.example.Article;

import java.util.Arrays;

public class AvgWordLength {
    public static double averageWordLength(Article article) {
        String text = article.getText();
        if (text == null || text.isBlank()) {
            return 0.0;
        }

        return Arrays.stream(text.split("\\s+"))
                .map(word -> word.replaceAll("[^\\p{L}]", ""))
                .filter(word -> !word.isEmpty())
                .mapToInt(String::length)
                .average()
                .orElse(0.0);
    }
}
