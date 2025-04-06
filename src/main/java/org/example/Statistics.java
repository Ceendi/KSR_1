package org.example;

import java.util.Map;

public class Statistics {
    public static Double calculatePrecision(Map<Article, String> classifiedArticles) {
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
        return (double) TP / (TP + FN);
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
        return (double) TP / (TP + FP);
    }
}
