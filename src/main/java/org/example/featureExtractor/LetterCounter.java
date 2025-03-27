package org.example.featureExtractor;

import org.example.Article;

import java.util.Arrays;

public class LetterCounter {
    public static int countWords(Article article) {
        String text = article.getText();
        return (text == null || text.isBlank()) ? 0 : Arrays.stream(text.split("\\s+"))
                .mapToInt(word -> word.replaceAll("[^\\p{L}]", "").length())
                .sum();
    }
}
