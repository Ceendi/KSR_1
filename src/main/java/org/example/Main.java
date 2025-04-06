package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws IOException {
       double splitPercentage = 0.5;
       int neighboursNumber = 10;
       BiFunction<Article, Article, Double> metricFunction = Metrics::euclideanDistance;

        List<String> fileNames;
        try (Stream<Path> paths = Files.list(Path.of("data/"))) {
           fileNames = paths
                   .map(Path::toString)
                   .filter(fileName -> fileName.toString().startsWith("data\\reut2"))
                   .toList()
                   .subList(17, 18);
       } catch (IOException e) {
           throw new RuntimeException(e);
       }

        System.out.println(fileNames);

        Serializer.saveArticlesToFile(fileNames);
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

        System.out.println("Accuracy: \t " + Statistics.calculatePrecision(articleLableMap));

        for (String label : labels) {
            System.out.println(label + "\t Recall: \t " + Statistics.calculateRecall(articleLableMap, label));
            System.out.println(label + "\t Precision: \t " + Statistics.calculatePrecision(articleLableMap, label));
        }
    }
}