package org.example.featureExtractor;

import org.example.Article;

import java.util.*;

public class MostFrequentKeyword {
    public static String mostFrequentKeyword(Article article, List<String> keywords) {
        String text = article.getText().toLowerCase();
        Map<String, Integer> countryCount = new HashMap<>();

        for (String country : keywords) {
            String lowerCountry = country.toLowerCase();
            int count = (int) Arrays.stream(text.split("\\s+"))
                    .map(word -> word.replaceAll("[^\\p{L}]", ""))
                    .filter(word -> word.equals(lowerCountry))
                    .count();
            if (count > 0) {
                countryCount.put(country, count);
            }
        }

        return countryCount.entrySet().stream()
                .max(Comparator.comparingInt((Map.Entry<String, Integer> e) -> e.getValue()).thenComparingInt(e -> keywords.indexOf(e.getKey())))
                .map(Map.Entry::getKey)
                .orElse("Brak kraju");
    }
}
