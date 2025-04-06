package org.example;

import java.util.List;

public class Normalizator {
    public static void normalizeArticles(List<Article> articles) {
        if (articles == null || articles.isEmpty()) {
            return;
        }

        // Oblicz maksymalne wartości dla każdej cechy liczbowej
        double maxLetterCount = articles.stream()
                .mapToDouble(article -> article.getFeatures().getLetterCount())
                .max()
                .orElse(1);

        double maxAvgWordLength = articles.stream()
                .mapToDouble(article -> article.getFeatures().getAvgWordLength())
                .max()
                .orElse(1.0);

        double maxAvgSentenceLength = articles.stream()
                .mapToDouble(article -> article.getFeatures().getAvgSentenceLength())
                .max()
                .orElse(1.0);

        double maxUniqueWordRatio = articles.stream()
                .mapToDouble(article -> article.getFeatures().getUniqueWordRatio())
                .max()
                .orElse(1.0);

        double maxFRE = articles.stream()
                .mapToDouble(article -> article.getFeatures().getFRE())
                .max()
                .orElse(1.0);

        // Oblicz minimalne wartości dla każdej cechy liczbowej
        double minLetterCount = articles.stream()
                .mapToDouble(article -> article.getFeatures().getLetterCount())
                .min()
                .orElse(0.0);

        double minAvgWordLength = articles.stream()
                .mapToDouble(article -> article.getFeatures().getAvgWordLength())
                .min()
                .orElse(0.0);

        double minAvgSentenceLength = articles.stream()
                .mapToDouble(article -> article.getFeatures().getAvgSentenceLength())
                .min()
                .orElse(0.0);

        double minUniqueWordRatio = articles.stream()
                .mapToDouble(article -> article.getFeatures().getUniqueWordRatio())
                .min()
                .orElse(0.0);

        double minFRE = articles.stream()
                .mapToDouble(article -> article.getFeatures().getFRE())
                .min()
                .orElse(0.0);

        // Dla każdego artykułu dokonujemy normalizacji cech liczbowych
        for (Article article : articles) {
            Features features = article.getFeatures();

            // Normalizacja – dzielimy przez wartość maksymalną danej cechy
            double normalizedLetterCount = (features.getLetterCount() - minLetterCount) / (maxLetterCount - minLetterCount);
            double normalizedAvgWordLength = (features.getAvgWordLength() - minAvgWordLength) / (maxAvgWordLength - minAvgWordLength);
            double normalizedAvgSentenceLength = (features.getAvgSentenceLength() - minAvgSentenceLength) / (maxAvgSentenceLength - minAvgSentenceLength);
            double normalizedUniqueWordRatio = (features.getUniqueWordRatio() - minUniqueWordRatio) / (maxUniqueWordRatio - minUniqueWordRatio);
            double normalizedFRE = (features.getFRE() - minFRE) / (maxFRE - minFRE);

            // Tworzymy nową instancję Features z znormalizowanymi wartościami liczbowymi
            // Pozostałe (nieliczbowe) cechy kopiujemy bez zmian
            Features normalizedFeatures = new Features(
                    normalizedLetterCount,
                    normalizedAvgWordLength,
                    normalizedAvgSentenceLength,
                    normalizedUniqueWordRatio,
                    features.getMostCommonSurname(),
                    features.getMostCommonCurrency(),
                    features.getMostCommonCountry(),
                    features.getMostCommonUnitSystem(),
                    features.getMostFrequentCapitalized(),
                    normalizedFRE
            );

            // Aktualizujemy artykuł nowymi cechami
            article.setFeatures(normalizedFeatures);
        }
    }
}
