package org.example.classifier;


import org.example.Article;

import java.util.function.BiFunction;


public class Metrics {
    public static final BiFunction<Article, Article, Double> EUCLIDEAN = Metrics::euclideanDistance;
    public static final BiFunction<Article, Article, Double> CHEBYSHEV = Metrics::chebyshevDistance;
    public static final BiFunction<Article, Article, Double> MANHATTAN = Metrics::manhattanDistance;

    public static Double euclideanDistance(Article a, Article b) {
        double distance = 0.0;

        for (String key: a.getFeatureMap().keySet()) {
            Object v1 = a.getFeatureMap().get(key);
            Object v2 = b.getFeatureMap().get(key);

            if (v1 instanceof Number && v2 instanceof Number) {
                distance += Math.pow(((Number) v1).doubleValue() - ((Number) v2).doubleValue(), 2);
            } else if (v1 instanceof String && v2 instanceof String) {
                distance += Math.pow(1 - Measurements.generalizedNGramDistance((String) v1, (String) v2), 2);
            } else if (v1 instanceof Boolean && v2 instanceof Boolean) {
                distance += Math.pow(((Boolean) v1 ? 1 : 0) - ((Boolean) v2 ? 1 : 0), 2);
            } else {
                distance += 1;
            }
        }

        return Math.sqrt(distance);
    }

    public static Double chebyshevDistance(Article a, Article b) {
        double distance = 0.0;

        for (String key: a.getFeatureMap().keySet()) {
            Object v1 = a.getFeatureMap().get(key);
            Object v2 = b.getFeatureMap().get(key);

            if (v1 instanceof Number && v2 instanceof Number) {
                distance += Math.max(distance, Math.abs(((Number) v1).doubleValue() - ((Number) v2).doubleValue()));
            } else if (v1 instanceof String && v2 instanceof String) {
                distance += Math.max(distance, 1 - Measurements.generalizedNGramDistance((String) v1, (String) v2));
            } else if (v1 instanceof Boolean && v2 instanceof Boolean) {
                distance += Math.max(distance, Math.abs(((Boolean) v1 ? 1 : 0) - ((Boolean) v2 ? 1 : 0)));
            } else {
                distance += Math.max(distance, 1);
            }
        }

        return distance;
    }

    public static Double manhattanDistance(Article a, Article b) {
        double distance = 0.0;

        for (String key: a.getFeatureMap().keySet()) {
            Object v1 = a.getFeatureMap().get(key);
            Object v2 = b.getFeatureMap().get(key);

            if (v1 instanceof Number && v2 instanceof Number) {
                distance += Math.abs(((Number) v1).doubleValue() - ((Number) v2).doubleValue());
            } else if (v1 instanceof String && v2 instanceof String) {
                distance += 1 - Measurements.generalizedNGramDistance((String) v1, (String) v2);
            } else if (v1 instanceof Boolean && v2 instanceof Boolean) {
                distance += Math.abs(((Boolean) v1 ? 1 : 0) - ((Boolean) v2 ? 1 : 0));
            } else {
                distance += 1;
            }
        }

        return distance;
    }

    public static String getMetricName(BiFunction<Article, Article, Double> metric) {

        if (metric.equals(EUCLIDEAN)) {
            return "Metryka Euklidesa";
        } else if (metric.equals(CHEBYSHEV)) {
            return "Metryka Czebyszewa";
        } else if (metric.equals(MANHATTAN)) {
            return "Metryka Manhattan";
        }
        return "Nieznana metryka";
    }
}
