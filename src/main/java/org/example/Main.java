package org.example;

import org.example.classifier.KNNClassifier;
import org.example.classifier.Metrics;
import org.example.classifier.Statistics;
import org.example.classifier.Normalizator;
import org.example.extractor.Serializer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.example.classifier.Metrics.getMetricName;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        List<String> selectedFeatureNames = initDefaultFeatures();

        int neighboursNumber = promptNeighbours(scanner);
        double splitPercentage = promptSplit(scanner);
        BiFunction<Article, Article, Double> metricFunction = promptMetric(scanner);
        promptFeatureRemoval(selectedFeatureNames, scanner);
        // extractFeaturesAndSaveToFile(22); // only uncomment when you want to extract new features
        List<Article> articles = loadArticles();

        filterFeatures(articles, selectedFeatureNames);
        Normalizator.normalizeArticles(articles);

        List<Article> trainArticles = articles.subList(0, (int) (articles.size() * splitPercentage));
        List<Article> testArticles = articles.subList((int) (articles.size() * splitPercentage), articles.size());

        Map<Article, String> articleLableMap = classify(neighboursNumber, trainArticles, testArticles, metricFunction);

        printStatistics(articles, metricFunction, neighboursNumber, splitPercentage, articleLableMap);
    }

    private static Map<Article, String> classify(int neighboursNumber, List<Article> trainArticles, List<Article> testArticles, BiFunction<Article, Article, Double> metricFunction) {
        KNNClassifier classifier = new KNNClassifier(neighboursNumber);
        classifier.train(trainArticles);
        Map<Article, String> articleLableMap = new HashMap<>();
        for (Article article : testArticles) {
            articleLableMap.put(article, classifier.classify(article, metricFunction));
        }
        return articleLableMap;
    }

    private static void printStatistics(
            List<Article> articles,
            BiFunction<Article, Article, Double> metricFunction,
            int neighboursNumber,
            double splitPercentage,
            Map<Article, String> articleLabelMap) {

        Set<String> labels = articles.stream()
                .map(Article::getLabel)
                .collect(Collectors.toSet());

        List<String> metricNames = List.of("Accuracy", "Recall", "Precision", "F1");

        int maxLabelWidth = labels.stream()
                .mapToInt(String::length)
                .max()
                .orElse(0);

        int maxMetricNameWidth = metricNames.stream()
                .mapToInt(String::length)
                .max()
                .orElse(0);

        int valueWidth = 8;

        System.out.println(
                String.format("Wyniki dla k=%d, podział: %.1f%%/%.1f%%, metryka='%s'",
                        neighboursNumber,
                        splitPercentage * 100,
                        (1 - splitPercentage) * 100,
                        getMetricName(metricFunction))
        );
        System.out.println();

        String accuracyLabel = "Accuracy";
        double accuracy = Statistics.calculateAccuracy(articleLabelMap);
        System.out.printf(
                "%-" + maxLabelWidth + "s │ %-" + maxMetricNameWidth + "s = %" + valueWidth + ".6f%n",
                "", accuracyLabel, accuracy
        );
        System.out.println();

        for (String label : labels) {
            double recall = Statistics.calculateRecall(articleLabelMap, label);
            double precision = Statistics.calculatePrecision(articleLabelMap, label);
            double f1 = 2 * recall * precision / (recall + precision);

            System.out.printf(
                    "%-" + maxLabelWidth + "s │ %-" + maxMetricNameWidth + "s = %" + valueWidth + ".6f%n",
                    label, "Recall", recall
            );
            System.out.printf(
                    "%-" + maxLabelWidth + "s │ %-" + maxMetricNameWidth + "s = %" + valueWidth + ".6f%n",
                    label, "Precision", precision
            );
            System.out.printf(
                    "%-" + maxLabelWidth + "s │ %-" + maxMetricNameWidth + "s = %" + valueWidth + ".6f%n",
                    label, "F1", f1
            );

            System.out.println();
        }

        System.out.println("Tablica pomyłek:");
        printConfusionMatrix(articleLabelMap, labels);
    }


//    private static void printConfusionMatrix(Map<Article, String> articleLableMap, Set<String> labels) {
//        int[][] confusionMatrix = Statistics.calculateConfusionMatrix(articleLableMap, labels);
//        List<String> labelList = new ArrayList<>(Arrays.asList("us", "ca", "jp", "uk", "fr", "wg"));

    /// /        System.out.println("\tus \tca \tjp \tuk \tfr \twg \t");
//        for (int i = 0; i < labels.size(); i++) {
//            System.out.print("\t" + labelList.get(i));
//        }
//        System.out.println();
//        for (int i = 0; i < confusionMatrix.length; i++) {
//            System.out.print(labelList.get(i) + "\t");
//            for (int j = 0; j < confusionMatrix[i].length; j++) {
//                System.out.print(confusionMatrix[i][j] + " " + "\t");
//            }
//            System.out.println(); // Nowa linia po każdym wierszu
//        }
//    }
    private static void printConfusionMatrix(Map<Article, String> articleLabelMap, Set<String> labels) {
        int[][] confusionMatrix = Statistics.calculateConfusionMatrix(articleLabelMap, labels);
        List<String> labelList = List.of("us", "ca", "jp", "uk", "fr", "wg");

        int maxLabelWidth = labelList.stream()
                .mapToInt(String::length)
                .max()
                .orElse(0);

        int maxNumberWidth = Arrays.stream(confusionMatrix)
                .flatMapToInt(row -> Arrays.stream(row))
                .map(n -> String.valueOf(n).length())
                .max()
                .orElse(0);

        int cellWidth = Math.max(maxLabelWidth, maxNumberWidth) + 2;

        System.out.printf("%" + cellWidth + "s", "");
        for (String lbl : labelList) {
            System.out.printf("%" + cellWidth + "s", lbl);
        }
        System.out.println();

        for (int i = 0; i < confusionMatrix.length; i++) {
            System.out.printf("%" + cellWidth + "s", labelList.get(i));
            for (int j = 0; j < confusionMatrix[i].length; j++) {
                System.out.printf("%" + cellWidth + "d", confusionMatrix[i][j]);
            }
            System.out.println();
        }
    }


    private static void filterFeatures(List<Article> articles, List<String> selectedFeatureNames) {
        articles.forEach(art -> art.getFeatureMap()
                .keySet()
                .removeIf(key -> !selectedFeatureNames.contains(key))
        );
    }

    private static List<Article> loadArticles() {
        return Serializer.readArticlesFromFile();
    }

    private static void extractFeaturesAndSaveToFile(int fileCount) {
        List<String> fileNames;
        try (Stream<Path> paths = Files.list(Path.of("data/"))) {
            fileNames = paths
                    .map(Path::toString)
                    .filter(fileName -> fileName.startsWith("data\\reut2"))
                    .toList()
                    .subList(0, fileCount);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Serializer.saveArticlesToFile(fileNames);
    }

    private static void promptFeatureRemoval(List<String> selectedFeatureNames, Scanner scanner) {
        System.out.println("Aktualnie wybrane cechy do klasyfikacji:");
        for (int i = 0; i < selectedFeatureNames.size(); i++) {
            System.out.println((i + 1) + ". " + selectedFeatureNames.get(i));
        }

        System.out.print("Podaj cechy do usunięcia (oddziel je spacjami): ");
        String input = scanner.nextLine();

        if (!input.trim().isEmpty()) {
            String[] featureNumbers = input.split(" ");
            List<String> featuresToRemove = new ArrayList<>();
            for (String featureNumber : featureNumbers) {
                try {
                    int index = Integer.parseInt(featureNumber) - 1;
                    if (index >= 0 && index < selectedFeatureNames.size()) {
                        featuresToRemove.add(selectedFeatureNames.get(index));
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Błędny numer: " + featureNumber);
                }
            }
            selectedFeatureNames.removeAll(featuresToRemove);
        }
    }

    private static BiFunction<Article, Article, Double> promptMetric(Scanner scanner) {
        System.out.println("Wybierz metrykę:");
        System.out.println("1. Euclidean");
        System.out.println("2. Chebyshev");
        System.out.println("3. Manhattan");
        System.out.print("Twój wybór: ");
        int metricChoice = scanner.nextInt();
        scanner.nextLine();

        return switch (metricChoice) {
            case 2 -> Metrics.CHEBYSHEV;
            case 1 -> Metrics.EUCLIDEAN;
            case 3 -> Metrics.MANHATTAN;
            default -> throw new IllegalArgumentException("Nieprawidłowy wybór metryki.");
        };
    }

    private static double promptSplit(Scanner scanner) {
        double splitPercentage = -1.0;
        while (splitPercentage < 0.0 || splitPercentage > 1.0) {
            System.out.print("Podaj procent artykułów przeznaczonych na trening (w przedziale 0 a 1): ");
            splitPercentage = scanner.nextDouble();
        }
        return splitPercentage;
    }

    private static int promptNeighbours(Scanner scanner) {
        System.out.print("Podaj liczbę sąsiadów (k): ");
        return scanner.nextInt();
    }

    private static List<String> initDefaultFeatures() {
        return new ArrayList<>(List.of(
                "letterCount",
                "avgWordLength",
                "avgSentenceLength",
                "uniqueWordRatio",
                "FRE",
                "mostCommonSurname",
                "mostCommonCountry",
                "mostCommonCurrency",
                "mostFrequentCapitalized",
                "mostCommonUnitSystem"
        ));
    }
}