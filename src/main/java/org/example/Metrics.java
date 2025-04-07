package org.example;


import java.util.List;


public class Metrics {
    public static final TriFunction<Article, Article, List<String>, Double> EUCLIDEAN = Metrics::euclideanDistance;
    public static final TriFunction<Article, Article, List<String>, Double> CHEBYSHEV = Metrics::chebyshevDistance;
    public static final TriFunction<Article, Article, List<String>, Double> MANHATTAN = Metrics::manhattanDistance;

    public static Double euclideanDistance(Article a, Article b, List<String> selectedFeatures) {
        double distance = 0.0;
        Features f1 = a.getFeatures();
        Features f2 = b.getFeatures();

        if (selectedFeatures.contains("FRE")) {
            distance += Math.pow(f1.getFRE() - f2.getFRE(), 2);
        }
        if (selectedFeatures.contains("avgSentenceLength")) {
            distance += Math.pow(f1.getAvgSentenceLength() - f2.getAvgSentenceLength(), 2);
        }
        if (selectedFeatures.contains("letterCount")) {
            distance += Math.pow(f1.getLetterCount() - f2.getLetterCount(), 2);
        }
        if (selectedFeatures.contains("avgWordLength")) {
            distance += Math.pow(f1.getAvgWordLength() - f2.getAvgWordLength(), 2);
        }
        if (selectedFeatures.contains("uniqueWordRatio")) {
            distance += Math.pow(f1.getUniqueWordRatio() - f2.getUniqueWordRatio(), 2);
        }
        if (selectedFeatures.contains("mostCommonCountry")) {
            distance += Math.pow(1 - generalizedNGramDistance(f1.getMostCommonCountry(), f2.getMostCommonCountry()), 2);
        }
        if (selectedFeatures.contains("mostCommonSurname")) {
            distance += Math.pow(1 - generalizedNGramDistance(f1.getMostCommonSurname(), f2.getMostCommonSurname()), 2);
        }
        if (selectedFeatures.contains("mostCommonCurrency")) {
            distance += Math.pow(1 - generalizedNGramDistance(f1.getMostCommonCurrency(), f2.getMostCommonCurrency()), 2);
        }
        if (selectedFeatures.contains("mostFrequentCapitalized")) {
            distance += Math.pow(1 - generalizedNGramDistance(f1.getMostFrequentCapitalized(), f2.getMostFrequentCapitalized()), 2);
        }
        if (selectedFeatures.contains("mostCommonUnitSystem")) {
            if (f1.getMostCommonUnitSystem() != null && f2.getMostCommonUnitSystem() != null) {
                distance += Math.pow((f1.getMostCommonUnitSystem() ? 1 : 0) - (f2.getMostCommonUnitSystem() ? 1 : 0), 2);
            } else {
                distance += 1;
            }
        }

        return Math.sqrt(distance);
    }

    public static Double chebyshevDistance(Article a, Article b, List<String> selectedFeatures) {
        Features f1 = a.getFeatures();
        Features f2 = b.getFeatures();

        double distance = 0.0;

        if (selectedFeatures.contains("letterCount")) {
            distance = Math.max(distance, Math.abs(f1.getLetterCount() - f2.getLetterCount()));
        }
        if (selectedFeatures.contains("avgWordLength")) {
            distance = Math.max(distance, Math.abs(f1.getAvgWordLength() - f2.getAvgWordLength()));
        }
        if (selectedFeatures.contains("avgSentenceLength")) {
            distance = Math.max(distance, Math.abs(f1.getAvgSentenceLength() - f2.getAvgSentenceLength()));
        }
        if (selectedFeatures.contains("uniqueWordRatio")) {
            distance = Math.max(distance, Math.abs(f1.getUniqueWordRatio() - f2.getUniqueWordRatio()));
        }
        if (selectedFeatures.contains("FRE")) {
            distance = Math.max(distance, Math.abs(f1.getFRE() - f2.getFRE()));
        }
        if (selectedFeatures.contains("mostCommonCountry")) {
            distance = Math.max(distance, 1 - generalizedNGramDistance(f1.getMostCommonCountry(), f2.getMostCommonCountry()));
        }
        if (selectedFeatures.contains("mostCommonSurname")) {
            distance = Math.max(distance, 1 - generalizedNGramDistance(f1.getMostCommonSurname(), f2.getMostCommonSurname()));
        }
        if (selectedFeatures.contains("mostCommonCurrency")) {
            distance = Math.max(distance, 1 - generalizedNGramDistance(f1.getMostCommonCurrency(), f2.getMostCommonCurrency()));
        }
        if (selectedFeatures.contains("mostFrequentCapitalized")) {
            distance = Math.max(distance, 1 - generalizedNGramDistance(f1.getMostFrequentCapitalized(), f2.getMostFrequentCapitalized()));
        }
        if (selectedFeatures.contains("mostCommonUnitSystem")) {
            double d10;
            if (f1.getMostCommonUnitSystem() != null && f2.getMostCommonUnitSystem() != null) {
                d10 = Math.abs((f1.getMostCommonUnitSystem() ? 1 : 0) - (f2.getMostCommonUnitSystem() ? 1 : 0));
            } else {
                d10 = 1;
            }
            distance = Math.max(distance, d10);
        }

        return distance;
    }

    public static Double manhattanDistance(Article a, Article b, List<String> selectedFeatures) {
        Features f1 = a.getFeatures();
        Features f2 = b.getFeatures();

        double distance = 0.0;

        if (selectedFeatures.contains("letterCount")) {
            distance += Math.abs(f1.getLetterCount() - f2.getLetterCount());
        }
        if (selectedFeatures.contains("avgWordLength")) {
            distance += Math.abs(f1.getAvgWordLength() - f2.getAvgWordLength());
        }
        if (selectedFeatures.contains("avgSentenceLength")) {
            distance += Math.abs(f1.getAvgSentenceLength() - f2.getAvgSentenceLength());
        }
        if (selectedFeatures.contains("uniqueWordRatio")) {
            distance += Math.abs(f1.getUniqueWordRatio() - f2.getUniqueWordRatio());
        }
        if (selectedFeatures.contains("FRE")) {
            distance += Math.abs(f1.getFRE() - f2.getFRE());
        }
        if (selectedFeatures.contains("mostCommonCountry")) {
            distance += Math.abs(1 - generalizedNGramDistance(f1.getMostCommonCountry(), f2.getMostCommonCountry()));
        }
        if (selectedFeatures.contains("mostCommonSurname")) {
            distance += Math.abs(1 - generalizedNGramDistance(f1.getMostCommonSurname(), f2.getMostCommonSurname()));
        }
        if (selectedFeatures.contains("mostCommonCurrency")) {
            distance += Math.abs(1 - generalizedNGramDistance(f1.getMostCommonCurrency(), f2.getMostCommonCurrency()));
        }
        if (selectedFeatures.contains("mostFrequentCapitalized")) {
            distance += Math.abs(1 - generalizedNGramDistance(f1.getMostFrequentCapitalized(), f2.getMostFrequentCapitalized()));
        }
        if (selectedFeatures.contains("mostCommonUnitSystem")) {
            double d10;
            if (f1.getMostCommonUnitSystem() != null && f2.getMostCommonUnitSystem() != null) {
                d10 = Math.abs((f1.getMostCommonUnitSystem() ? 1 : 0) - (f2.getMostCommonUnitSystem() ? 1 : 0));
            } else {
                d10 = 1;
            }
            distance += d10;
        }

        return distance;
    }

    public static Double generalizedNGramDistance(String a, String b) {
        if (a == null || b == null) {
            return 0.0;
        }
        a = a.toLowerCase();
        b = b.toLowerCase();

        int minLength = Math.min(a.length(), b.length());
        int maxLength = Math.max(a.length(), b.length());
        int n_1 = 2;
        int n_2 = minLength;


        double fun = (double) ((maxLength - n_1+1)*(maxLength-n_1+2)-(maxLength-n_2)*(maxLength-n_2+1))/2;
        int correct = 0;
        for (int i = n_1; i <= n_2; i++) {
            for (int j = 1; j <= a.length() - i + 1; j++) {
                if (b.contains(a.substring(j-1, j - 1 + i))) {
                    correct++;
                }
            }
        }

        return (double) correct / fun;
    }

    public static String getMetricName(TriFunction<Article, Article, List<String>, Double> metric) {

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
