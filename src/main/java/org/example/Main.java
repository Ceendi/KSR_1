package org.example;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
//        Files.readAll
//        ArrayList<String> fileNames = new ArrayList<>(List.of("data/reut2-000.sgm", "data/reut2-001.sgm",
//                "data/reut2-002.sgm", "data/reut2-003.sgm", "data/reut2-004.sgm",
//                "data/reut2-005.sgm", "data/reut2-006.sgm", "data/reut2-007.sgm",
//                "data/reut2-008.sgm", "data/reut2-009.sgm", "data/reut2-010.sgm",
//                "data/reut2-011.sgm", "data/reut2-012.sgm", "data/reut2-013.sgm",
//                "data/reut2-014.sgm", "data/reut2-015.sgm", "data/reut2-016.sgm",
//                "data/reut2-017.sgm", "data/reut2-018.sgm", "data/reut2-019.sgm",
//                "data/reut2-020.sgm", "data/reut2-021.sgm"));
//        Serializer.saveArticlesToFile(fileNames);
        List<Article> articles = Serializer.readArticlesFromFile();

        Normalizator.normalizeArticles(articles);

        KNNClassifier classifier = new KNNClassifier(10);
        classifier.train(articles.subList(0, articles.size() / 2));
        List<Article> testArticles = articles.subList(articles.size() / 2, articles.size());
        Map<Article, String> articleLableMap = new HashMap<>();
        for (Article article : testArticles) {
            articleLableMap.put(article, classifier.classify(article, Metrics::euclideanDistance));
        }
        System.out.println(articleLableMap);
        int correct = 0;
        int usa = 0;
        for (Map.Entry<Article, String> entry : articleLableMap.entrySet()) {
            if (entry.getKey().getLabel().equals("usa")) {
                usa++;
            }
            if (entry.getKey().getLabel().equals(entry.getValue())) {
                correct++;
            }
        }
        System.out.println((double) correct / articleLableMap.size());
        System.out.println((double) usa / articleLableMap.size());

        System.out.println("TPR");

        Set<String> labels = articles.stream().map(Article::getLabel).collect(Collectors.toSet());
        System.out.println(labels);

        for (String label : labels) {
            int TP = 0;
            int FN = 0;
            int FP = 0;
            for (Map.Entry<Article, String> entry : articleLableMap.entrySet()) {
                if (entry.getKey().getLabel().equals(label)) {
                    if (entry.getValue().equals(label)) {
                        TP++;
                    } else {
                        FN++;
                    }
                } else if (entry.getValue().equals(label)) {
                    FP++;
                }
            }
            System.out.println(label + "\t Recall: \t TP: " + TP + "\t FN:" + FN + "\t TPR: " + (double) TP / (TP + FN));
            System.out.println(label + "\t Precission: TP: " + TP + "\t FP: " + FP + "\t PPV: " + (double) TP / (TP + FP));
        }


//
//        List<Article> iLoveTesting = new ArrayList<>(articles.subList(0, 3));
//
//        Normalizator.normalizeArticles(iLoveTesting);
//
//        System.out.println(iLoveTesting);
//
//        System.out.println(Metrics.euclideanDistance(iLoveTesting.getFirst(), iLoveTesting.get(1)));
//        System.out.println();
    }
}