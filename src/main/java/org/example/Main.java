package org.example;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Article> articles = SGMParser.convert("data/reut2-000.sgm");

        for (Article article : articles) {
            Features features = FeatureExtractor.extractFeatures(article);
            article.setFeatures(features);
            System.out.println(article);
        }
    }
}