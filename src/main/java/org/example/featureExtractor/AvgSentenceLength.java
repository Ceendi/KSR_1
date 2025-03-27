package org.example.featureExtractor;

import org.example.Article;

import java.util.Arrays;

public class AvgSentenceLength {
    public static double averageSentenceLength(Article article) {
        String text = article.getText();
        String[] sentences = text.split("(?<=[.!?])\\s+");

        return Arrays.stream(sentences)
                .map(sentence -> sentence.replaceAll("[^\\p{L}\\s]", "").trim())
                .filter(sentence -> !sentence.isEmpty())
                .mapToInt(sentence -> sentence.split("\\s+").length)
                .average()
                .orElse(0.0);
    }
}
