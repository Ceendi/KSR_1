package org.example;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Article> articles = Serializer.readArticlesFromFile();

        List<Article> iLoveTesting = new ArrayList<>(articles.subList(0, 3));

        Normalizator.normalizeArticles(iLoveTesting);

        System.out.println(iLoveTesting);

        System.out.println(Metrics.euclideanDistance(iLoveTesting.getFirst(), iLoveTesting.getLast()));
    }
}