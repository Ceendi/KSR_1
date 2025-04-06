package org.example;

import java.io.IOException;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static org.example.Metrics.EUCLIDEAN;
import static org.example.Metrics.getMetricName;

public class Main {
    public static void main(String[] args) throws IOException {
       double splitPercentage = 0.5;
       int neighboursNumber = 10;
       BiFunction<Article, Article, Double> metricFunction = EUCLIDEAN;

//        List<String> fileNames;
//        try (Stream<Path> paths = Files.list(Path.of("data/"))) {
//           fileNames = paths
//                   .map(Path::toString)
//                   .filter(fileName -> fileName.toString().startsWith("data\\reut2"))
//                   .toList()
//                   .subList(0, 10);
//       } catch (IOException e) {
//           throw new RuntimeException(e);
//       }
//
//        Serializer.saveArticlesToFile(fileNames);
        List<Article> articles = Serializer.readArticlesFromFile();

        Normalizator.normalizeArticles(articles);

        KNNClassifier classifier = new KNNClassifier(neighboursNumber);
        List<Article> trainArticles = articles.subList(0, (int) (articles.size() * splitPercentage));
        List<Article> testArticles = articles.subList((int) (articles.size() * splitPercentage), articles.size());

        classifier.train(trainArticles);

        Map<Article, String> articleLableMap = new HashMap<>();
        for (Article article : testArticles) {
            articleLableMap.put(article, classifier.classify(article, metricFunction));
        }

        Set<String> labels = articles.stream().map(Article::getLabel).collect(Collectors.toSet());

        System.out.println("Wyniki dla k=" + neighboursNumber + ", podział na treningowy/uczący: " + (splitPercentage * 100) + "%/" + ((1 - splitPercentage) * 100) + "%, metryka='" + getMetricName(metricFunction) + "'");
        System.out.println("Accuracy: \t " + Statistics.calculateAccuracy(articleLableMap));

        for (String label : labels) {
            System.out.println(label + "\t Recall: \t " + Statistics.calculateRecall(articleLableMap, label));
            System.out.println(label + "\t Precision: \t " + Statistics.calculatePrecision(articleLableMap, label));
        }

        System.out.println("Tablica pomyłek");

        int[][] confusionMatrix = Statistics.calculateConfusionMatrix(articleLableMap, labels);
        List<String> labelList = new ArrayList<>(labels);
        System.out.print("\t");
        for (int i = 0; i < labels.size(); i++) {
            System.out.print("\t" + labelList.get(i));
        }
        System.out.println();
//        System.out.println("\tusa \tcanada \tjapan \tuk \tfrance \twest-germany \t");
        for (int i = 0; i < confusionMatrix.length; i++) {
            System.out.print(labelList.get(i) + "\t");
            for (int j = 0; j < confusionMatrix[i].length; j++) {
                System.out.print(confusionMatrix[i][j] + " \t");
            }
            System.out.println(); // Nowa linia po każdym wierszu
        }
    }
}