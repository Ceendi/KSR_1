package org.example.classifier;

import org.example.Article;

import java.util.*;
import java.util.stream.Collectors;


public class Normalizator {
    public static void normalizeArticles(List<Article> articles) {
        if (articles == null || articles.isEmpty()) {
            return;
        }

        Set<String> numericFeatures = articles.stream()
            .flatMap(a -> a.getFeatureMap().entrySet().stream())
            .filter(e -> e.getValue() instanceof Number)
            .map(Map.Entry::getKey)
            .collect(Collectors.toSet());

        Map<String, DoubleSummaryStatistics> stats = new HashMap<>();
        for (String feature : numericFeatures) {
            DoubleSummaryStatistics stat = articles.stream()
                .map(a -> a.getFeatureMap().get(feature))
                .filter(Objects::nonNull)
                .filter(v -> v instanceof Number)
                .mapToDouble(v -> ((Number) v).doubleValue())
                .summaryStatistics();
            stats.put(feature, stat);
        }

        for (Article article : articles) {
            for (String feature : numericFeatures) {
                Object valObj = article.getFeatureMap().get(feature);
                if (!(valObj instanceof Number)) {
                    continue;
                }
                double value = ((Number) valObj).doubleValue();
                DoubleSummaryStatistics stat = stats.get(feature);
                double min = stat.getMin();
                double max = stat.getMax();
                double normalized = (max - min) == 0.0 ? 0.0 : (value - min) / (max - min);
                article.setFeature(feature, normalized);
            }
        }
    }
}