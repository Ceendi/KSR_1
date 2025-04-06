package org.example;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.example.Metrics.getMetricName;

public class Main {
    public static void main(String[] args) throws IOException {
        List<String> selectedFeatureNames = new ArrayList<>(List.of(
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

        Scanner scanner = new Scanner(System.in);

        //---------------------------K---------------------------
        System.out.print("Podaj liczbę sąsiadów (k): ");
        int neighboursNumber = scanner.nextInt();
        scanner.nextLine();
        //-------------------------------------------------------

        //----------------------PODZIAL--------------------------
        double splitPercentage = -1.0;
        while (splitPercentage < 0.0 || splitPercentage > 1.0) {
            System.out.print("Podaj procent artykułów przeznaczonych na trening (w przedziale 0 a 1): ");
            splitPercentage = scanner.nextDouble();
        }
        //-------------------------------------------------------

        //--------------------METRYKI-----------------------------
        System.out.println("Wybierz metrykę:");
        System.out.println("1. Euclidean");
        System.out.println("2. Chebyshev");
        System.out.println("3. Manhattan");
        System.out.print("Twój wybór: ");
        int metricChoice = scanner.nextInt();
        scanner.nextLine();

        TriFunction<Article, Article, List<String>, Double> metricFunction = switch (metricChoice) {
            case 2 -> Metrics.CHEBYSHEV;
            case 3 -> Metrics.MANHATTAN;
            default -> Metrics.EUCLIDEAN;
        };
        //--------------------------------------------------------

        //-------------------------CECHY--------------------------
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

        
        //--------------------------------------------------------

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
            articleLableMap.put(article, classifier.classify(article, metricFunction, selectedFeatureNames));
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