package org.example.featureExtractor;

import org.example.Article;

import java.util.Arrays;

public class UnqWordRatio {
    public static double uniqueWordRatio(Article article) {
        String text = article.getText();
        if (text == null || text.isBlank()) {
            return 0.0;
        }
        String[] words = text.split("\\s+");
        long totalWords = words.length;
        long uniqueWords = Arrays.stream(words)
                .map(word -> word.replaceAll("[^\\p{L}]", "").toLowerCase())
                .filter(word -> !word.isEmpty())
                .distinct()
                .count();
        return totalWords == 0 ? 0.0 : (double) uniqueWords / totalWords;
    }
}
