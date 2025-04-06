package org.example;

import java.util.*;
import java.util.function.BiFunction;

public class KNNClassifier {
    private List<Article> trainingArticles; // Lista artykułów treningowych
    private int k; // Liczba sąsiadów

    public KNNClassifier(int k) {
        this.k = k;
        this.trainingArticles = new ArrayList<>();
    }

    // Metoda trenowania - dodanie artykułów do listy
    public void train(List<Article> articles) {
        this.trainingArticles.addAll(articles);
    }

    // Metoda klasyfikowania nowego artykułu
    public String classify(Article newArticle, BiFunction<Article, Article, Double> distanceFunction) {
        List<Map.Entry<Article, Double>> distances = new ArrayList<>();

        // Obliczanie odległości do wszystkich artykułów w zbiorze treningowym
        for (Article trainingArticle : trainingArticles) {
            double distance = distanceFunction.apply(newArticle, trainingArticle);
            distances.add(new AbstractMap.SimpleEntry<>(trainingArticle, distance));
        }

        // Sortowanie artykułów według odległości
        distances.sort(Comparator.comparingDouble(Map.Entry::getValue));

        // Wybieranie k najbliższych sąsiadów
        Map<String, Integer> labelVotes = new HashMap<>();
        for (int i = 0; i < k; i++) {
            String label = distances.get(i).getKey().getLabel();
            labelVotes.put(label, labelVotes.getOrDefault(label, 0) + 1);
        }

        return resolveVotes(labelVotes, distances.subList(0, k));
    }

    // Funkcja pomocnicza do uzyskania etykiety z największą liczbą głosów
    public String resolveVotes(Map<String, Integer> votes, List<Map.Entry<Article, Double>> distances) {
        List<String> candidates = new ArrayList<>();
        int maxVotes = Collections.max(votes.entrySet(), Map.Entry.comparingByValue()).getValue();


        for (Map.Entry<String, Integer> entry : votes.entrySet()) {
            if (entry.getValue() == maxVotes) {
                candidates.add(entry.getKey());
            }
        }

        if (candidates.size() == 1) {
            return candidates.getFirst();
        } else {
            Map<String, Double> candidateDistances = new HashMap<>();

            for (Map.Entry<Article, Double> distance : distances) {
                if (candidates.contains(distance.getKey().getLabel())) {
                    candidateDistances.put(distance.getKey().getLabel(),
                            candidateDistances.getOrDefault(
                                    distance.getKey().getLabel(), 0.0) + distance.getValue()
                    );
                }
            }

            return candidateDistances
                    .entrySet()
                    .stream()
                    .min(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse(null);
        }
    }
}
