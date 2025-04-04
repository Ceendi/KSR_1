package org.example;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Article> articles = SGMParser.convert("data/reut2-000.sgm");

        for (Article article : articles) {
            Features features = FeatureExtractor.extractFeatures(article);

            System.out.println("--------------------------------------------");
            System.out.println("Text: " + article.getText());
            System.out.println("Label: " + article.getLabel());
            System.out.println(features);
        }
    }
}