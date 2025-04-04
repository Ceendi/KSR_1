package org.example;

import java.util.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SGMParser {
    public final static List<String> classifiedCountries = new LinkedList<>(List.of("west-germany", "usa", "france", "uk", "canada", "japan"));
    public static List<Article> convert(String fileName) {
        Path filePath = Path.of(fileName);
        ArrayList<Article> articles = new ArrayList<>();
        try {
            String content = Files.readString(filePath);

            Pattern reutersPattern = Pattern.compile("<REUTERS(.*?)</REUTERS>", Pattern.DOTALL);
            Matcher reutersMatcher = reutersPattern.matcher(content);

            while (reutersMatcher.find()) {
                String article = reutersMatcher.group(1);

                //BODY
                Pattern bodyPattern = Pattern.compile("<BODY>(.*?)</BODY>", Pattern.DOTALL);
                Matcher bodyMatcher = bodyPattern.matcher(article);
                String bodyText = "";
                if (bodyMatcher.find()) {
                    bodyText = bodyMatcher.group(1).trim()
                            .replaceAll("(?i)\\s*REUTER\\s*[&?#3;]*$", "");
                }

                //PLACES
                Pattern placesPattern = Pattern.compile("<PLACES>(.*?)</PLACES>", Pattern.DOTALL);
                Matcher placesMatcher = placesPattern.matcher(article);
                ArrayList<String> placesArray = new ArrayList<>();
                if (placesMatcher.find()) {
                    String placesText = placesMatcher.group(1).trim();

                    //Wyciąganie z tagów <D>
                    Pattern dPattern = Pattern.compile("<D>(.*?)</D>", Pattern.DOTALL);
                    Matcher dMatcher = dPattern.matcher(placesText);
                    while (dMatcher.find()) {
                        placesArray.add(dMatcher.group(1).trim());
                    }
                }

                if (placesArray.size() == 1 && !bodyText.isEmpty() && classifiedCountries.contains(placesArray.getFirst())) {
                    articles.add(new Article(bodyText, placesArray.getFirst()));
                }
            }
        } catch (IOException e) {
            System.err.println("Błąd podczas odczytu pliku: " + e.getMessage());
        }

        return articles;
    }
}
