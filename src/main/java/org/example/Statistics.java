package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Statistics {
    public static Double calculateAccuracy(Map<Article, String> classifiedArticles) {
        int correct = 0;
        for (Map.Entry<Article, String> entry : classifiedArticles.entrySet()) {
            if (entry.getKey().getLabel().equals(entry.getValue())) {
                correct++;
            }
        }
        return correct / (double) classifiedArticles.size();
    }

    public static Double calculateRecall(Map<Article, String> classifiedArticles, String label) {
        int TP = 0;
        int FN = 0;
        for (Map.Entry<Article, String> entry : classifiedArticles.entrySet()) {
            //sprawdzamy USA i article jest USA
            if (entry.getKey().getLabel().equals(label)) {
                //z klasyfikacji wyszło USA - TP
                if (entry.getValue().equals(label)) {
                    TP++;
                    //z klasyfikacji wyszły Niemcy - FN
                } else {
                    FN++;
                }
            }
        }
        return (TP + FN) == 0 ? 0.0 : (double) TP / (TP + FN);
    }

    public static Double calculatePrecision(Map<Article, String> classifiedArticles, String label) {
        int TP = 0;
        int FP = 0;
        for (Map.Entry<Article, String> entry : classifiedArticles.entrySet()) {
            //sprawdzamy USA i article jest USA
            if (entry.getKey().getLabel().equals(label)) {
                //z klasyfikacji wyszło USA - TP
                if (entry.getValue().equals(label)) {
                    TP++;
                }
                //sprawdzamy USA i article jest Niemcy a z klasyfikacji wyszły USA - FP
            } else if (entry.getValue().equals(label)) {
                FP++;
            }
        }
        return (TP + FP) == 0 ? 0.0 : (double) TP / (TP + FP);
    }

    public static Double calculateF1Score(Map<Article, String> classifiedArticles) {
        return 0.;
    }

    public static int[][] calculateConfusionMatrix(Map<Article, String> classifiedArticles, Set<String> labels) {
        int[][] confusionMatrix = new int[6][6];
        List<String> labelList = new ArrayList<>(labels);
        for (int i = 0; i < labels.size(); i++) {
            for (Map.Entry<Article, String> entry : classifiedArticles.entrySet()) {
                if (entry.getKey().getLabel().equals(labelList.get(i))) {
                    confusionMatrix[i][labelList.indexOf(entry.getValue())]++;
                }
            }
        }
        return confusionMatrix;
    }
}
