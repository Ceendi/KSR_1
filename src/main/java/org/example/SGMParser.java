package org.example;

import org.example.featureExtractor.*;

import java.util.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SGMParser {
    public static List<Article> convert(String fileName) {
        Path filePath = Path.of(fileName);
        ArrayList<Article> articles = new ArrayList<>();
        try {
            String content = Files.readString(filePath);

            Pattern reutersPattern = Pattern.compile("<REUTERS(.*?)</REUTERS>", Pattern.DOTALL);
            Matcher reutersMatcher = reutersPattern.matcher(content);

            int articleCount = 1;
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

                if (placesArray.size() == 1 && !bodyText.isEmpty()) {
                    articles.add(new Article(bodyText, placesArray.getFirst()));

                    System.out.println("Article " + articleCount + ":");
                    System.out.println("BODY:");
                    System.out.println(bodyText);
                    System.out.println("LABEL:");
                    System.out.println(String.join(" ", placesArray));
                    System.out.println("AVERAGE SENTENCE LENGTH");
                    System.out.println(AvgSentenceLength.averageSentenceLength(articles.getLast()));
                    System.out.println("AVERAGE WORD LENGTH");
                    System.out.println(AvgWordLength.averageWordLength(articles.getLast()));
                    System.out.println("UNIQUE WORD RATIO");
                    System.out.println(UnqWordRatio.uniqueWordRatio(articles.getLast()));
                    System.out.println("LETTER COUNTER");
                    System.out.println(LetterCounter.countWords(articles.getLast()));
                    System.out.println("MOST FREQUENT COUNTRY");
                    System.out.println(MostFrequentKeyword.mostFrequentKeyword(articles.getLast(), Files.readAllLines(Path.of("data/all-places-strings.lc.txt"))));
                    System.out.println("MOST FREQUENT NAME");
                    System.out.println(MostFrequentKeyword.mostFrequentKeyword(articles.getLast(), Files.readAllLines(Path.of("data/all-people-strings.lc.txt"))));
                    System.out.println("MOST CAPITALIZED WORD");
                    System.out.println(MostFrequentCapitalized.mostFrequentCapitalizedWord(articles.getLast()));
                    System.out.println("UNIT SYSTEM");
                    System.out.println(UnitCounter.detectUnitSystem(articles.getLast()));
                    System.out.println("UNIT SYSTEM");
                    System.out.println(CurrencyCounter.mostFrequentCurrency(articles.getLast()));
                    System.out.println("-------------------------");

                    articleCount++;
                }
            }
        } catch (IOException e) {
            System.err.println("Błąd podczas odczytu pliku: " + e.getMessage());
        }

        return articles;
    }
}
