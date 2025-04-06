package org.example;


import java.util.function.BiFunction;

public class Metrics {
    public static final BiFunction<Article, Article, Double> EUCLIDEAN = Metrics::euclideanDistance;
    public static final BiFunction<Article, Article, Double> CHEBYSHEV = Metrics::chebyshevDistance;
    public static final BiFunction<Article, Article, Double> MANHATTAN = Metrics::manhattanDistance;

    public static Double euclideanDistance(Article a, Article b) {
        double distance = 0.0;
        Features f1 = a.getFeatures();
        Features f2 = b.getFeatures();

        distance += Math.pow(f1.getFRE() - f2.getFRE(), 2);
        distance += Math.pow(f1.getAvgSentenceLength() - f2.getAvgSentenceLength(), 2);
        distance += Math.pow(f1.getLetterCount() - f2.getLetterCount(), 2);
        distance += Math.pow(f1.getAvgWordLength() - f2.getAvgWordLength(), 2);
        distance += Math.pow(f1.getUniqueWordRatio() - f2.getUniqueWordRatio(), 2);
        distance += Math.pow(1 - generalizedNGramDistance(f1.getMostCommonCountry(), f2.getMostCommonCountry()), 2);
        distance += Math.pow(1 - generalizedNGramDistance(f1.getMostCommonSurname(), f2.getMostCommonSurname()), 2);
        distance += Math.pow(1 - generalizedNGramDistance(f1.getMostCommonCurrency(), f2.getMostCommonCurrency()), 2);
        distance += Math.pow(1 - generalizedNGramDistance(f1.getMostFrequentCapitalized(), f2.getMostFrequentCapitalized()), 2);
        if (f1.getMostCommonUnitSystem() != null && f2.getMostCommonUnitSystem() != null) {
            distance += Math.pow((f1.getMostCommonUnitSystem() ? 1 : 0) - (f2.getMostCommonUnitSystem() ? 1 : 0), 2);
        } else {
            distance += 1;
        }

        return Math.sqrt(distance);
    }

    public static Double chebyshevDistance(Article a, Article b) {
        Features f1 = a.getFeatures();
        Features f2 = b.getFeatures();

        double d1 = Math.abs(f1.getLetterCount() - f2.getLetterCount());
        double d2 = Math.abs(f2.getAvgWordLength() - f1.getAvgWordLength());
        double d3 = Math.abs(f2.getAvgSentenceLength() - f1.getAvgSentenceLength());
        double d4 = Math.abs(f2.getUniqueWordRatio() - f1.getUniqueWordRatio());
        double d5 = Math.abs(f2.getFRE() - f1.getFRE());
        double d6 = 1 - generalizedNGramDistance(f1.getMostCommonCountry(), f2.getMostCommonCountry());
        double d7 = 1 - generalizedNGramDistance(f1.getMostCommonSurname(), f2.getMostCommonSurname());
        double d8 = 1 - generalizedNGramDistance(f1.getMostCommonCurrency(), f2.getMostCommonCurrency());
        double d9 = 1 - generalizedNGramDistance(f1.getMostFrequentCapitalized(), f2.getMostFrequentCapitalized());
        double d10;
        if (f1.getMostCommonUnitSystem() != null && f2.getMostCommonUnitSystem() != null) {
            d10 = Math.abs((f1.getMostCommonUnitSystem() ? 1 : 0) - (f2.getMostCommonUnitSystem() ? 1 : 0));
        } else {
            d10 = 1;
        }
        return Math.max(d1, Math.max(d2, Math.max(d3, Math.max(d4, Math.max(d5,
                Math.max(d6, Math.max(d7, Math.max(d8, Math.max(d9, d10)))))))));
    }

    public static Double manhattanDistance(Article a, Article b) {
        Features f1 = a.getFeatures();
        Features f2 = b.getFeatures();

        double d1 = Math.abs(f1.getLetterCount() - f2.getLetterCount());
        double d2 = Math.abs(f2.getAvgWordLength() - f1.getAvgWordLength());
        double d3 = Math.abs(f2.getAvgSentenceLength() - f1.getAvgSentenceLength());
        double d4 = Math.abs(f2.getUniqueWordRatio() - f1.getUniqueWordRatio());
        double d5 = Math.abs(f2.getFRE() - f1.getFRE());
        double d6 = 1 - generalizedNGramDistance(f1.getMostCommonCountry(), f2.getMostCommonCountry());
        double d7 = 1 - generalizedNGramDistance(f1.getMostCommonSurname(), f2.getMostCommonSurname());
        double d8 = 1 - generalizedNGramDistance(f1.getMostCommonCurrency(), f2.getMostCommonCurrency());
        double d9 = 1 - generalizedNGramDistance(f1.getMostFrequentCapitalized(), f2.getMostFrequentCapitalized());
        double d10;
        if (f1.getMostCommonUnitSystem() != null && f2.getMostCommonUnitSystem() != null) {
            d10 = Math.abs((f1.getMostCommonUnitSystem() ? 1 : 0) - (f2.getMostCommonUnitSystem() ? 1 : 0));
        } else {
            d10 = 1;
        }
        return d1 + d2 + d3 + d4 + d5 + d6 + d7 + d8 + d9 + d10;
    }

    public static Double generalizedNGramDistance(String a, String b) {
        if (a == null || b == null) {
            return 0.0;
        }
        a = a.toLowerCase();
        b = b.toLowerCase();
        int minLength = Math.min(a.length(), b.length());

        int correct = 0;
        int total = 0;
        for (int i = 1; i <= minLength; i++) {
            for (int j = 0; j < minLength - i + 1; j++) {
                if (b.contains(a.substring(j, j + i))) {
                    correct++;
                }
                total++;
            }
        }

        return total == 0 ? 0.0 : (double) correct / total;
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
