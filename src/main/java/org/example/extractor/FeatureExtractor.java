package org.example.extractor;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.example.Article;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FeatureExtractor {
    private static final Set<String> currencySet = new HashSet<>(Arrays.asList(
            "dlr", "dlrs", "dollars", "dollar",
            "austdlr", "australian dlrs", "australian dlr", "australian dollar", "australian dollars",
            "hk", "hong kong dlrs", "hong kong dlr", "hong kong dollar", "hong kong dollars",
            "singdlr", "singapore dlrs", "singapore dlr", "singapore dollar", "signapore dollars",
            "nzdlr", "new zealand dlrs", "new zealand dlr", "new zealand dollar", "new zealand dollars",
            "can", "canadian dlrs", "canadian dlr", "canadian dollar", "canadian dollars",
            "stg", "british pound", "british pounds",
            "dmk", "marks", "mark",
            "yen",
            "sfr", "franc", "francs", "swiss franc", "swiss francs",
            "ffr", "french franc", "french francs",
            "bfr", "belgian franc", "belgian francs",
            "dfl", "florin", "florins", "guilder", "guilders", "dutch florin", "dutch florins", "dutch guilder", "dutch guilders",
            "lit", "lira", "liras", "italian lira", "italian liras",
            "dkr", "danish crown", "danish crowns", "crown", "crowns",
            "nkr", "norwegian crown", "norwegian crowns",
            "skr", "swedish crown", "swedish crowns",
            "mexpeso", "peso", "pesos", "mexican peso", "mexican pesos",
            "cruzado", "cruzados", "brazilian cruzado", "brazilian cruzados",
            "austral", "argentine austral", "australs", "argentine australs",
            "saudriyal", "saudi riyal", "saudi riyals", "riyals", "riyal",
            "rand", "rands",
            "rupiah", "rupiahs",
            "ringgit", "ringgits",
            "escudo", "escudos", "pescudo", "pescudos", "peseta", "pesetas",
            "drachma", "drachmas", "drachmes"
    ));
    private static final Set<String> metricSet = new HashSet<>(Arrays.asList(
            // długość
            "meter", "meters", "metre", "metres", "m",
            "centimeter", "centimeters", "centimetre", "centimetres", "cm",
            "kilometer", "kilometers", "kilometre", "kilometres", "km",
            // masa
            "gram", "grams", "g",
            "kilogram", "kilograms", "kg",
            // objętość
            "liter", "liters", "litre", "litres", "l",
            // temperatura
            "celsius", "°c"
    ));
    private static final Set<String> imperialSet = new HashSet<>(Arrays.asList(
            // długość
            "inch", "inches",
            "foot", "feet", "ft",
            "yard", "yards", "yd",
            "mile", "miles", "mi",
            // masa
            "pound", "pounds", "lb", "lbs",
            "ounce", "ounces", "oz",
            // temperatura
            "fahrenheit", "°f"
    ));
    private static final StanfordCoreNLP pipeline;
    private static final List<String> countryList;
    private static final List<String> surnameList;

    static {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner");
        pipeline = new StanfordCoreNLP(props);

        try {
            countryList = Files.readAllLines(Paths.get("data/all-places-strings.lc.txt"))
                    .stream()
                    .map(String::trim)
                    .collect(Collectors.toList());
            surnameList = Files.readAllLines(Paths.get("data/all-people-strings.lc.txt"))
                    .stream()
                    .map(String::trim)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void extractFeatures(Article article) {
        CoreDocument document = new CoreDocument(article.getText());
        pipeline.annotate(document);

        article.setFeature("letterCount", countTotalLetters(document));
        article.setFeature("avgWordLength", calculateAverageWordLength(document));
        article.setFeature("avgSentenceLength", calculateAverageSentenceLength(document));
        article.setFeature("uniqueWordRatio", calculateUniqueWordRatio(document));
        article.setFeature("mostFrequentCapitalized", findMostCommonCapitalizedWord(document));
        article.setFeature("mostCommonUnitSystem", determineUnitSystem(document));
        article.setFeature("mostCommonCurrency", findMostCommonCurrency(document));
        article.setFeature("mostCommonSurname", findMostCommonSurname(document));
        article.setFeature("mostCommonCountry", findMostCommonCountry(document));
        article.setFeature("FRE", calculateFleschReadingEase(document));
    }

    public static Double countTotalLetters(CoreDocument document) {
        int count = 0;

        for (CoreLabel token : document.tokens()) {
            String word = token.word();
            for (char c : word.toCharArray()) {
                if (Character.isLetter(c)) {
                    count++;
                }
            }
        }
        return (double) count;
    }

    public static Double calculateAverageWordLength(CoreDocument document) {
        int totalLength = 0;
        int wordCount = 0;

        for (CoreLabel token : document.tokens()) {
            String word = token.word();

            // tokeny które zawierają litery
            if (word.matches(".*[a-zA-Z].*")) {
                totalLength += word.length();
                wordCount++;
            }
        }

        return wordCount == 0 ? 0.0 : (double) totalLength / wordCount;
    }

    public static Double calculateAverageSentenceLength(CoreDocument document) {
        List<CoreSentence> sentences = document.sentences();
        if (sentences.isEmpty()) return 0.0;

        int totalWords = 0;

        for (CoreSentence sentence : sentences) {
            totalWords += sentence.tokens().size();
        }

        return (double) totalWords / sentences.size();
    }

    public static Double calculateUniqueWordRatio(CoreDocument document) {
        int totalWords = 0;
        Set<String> uniqueWords = new HashSet<>();

        for (CoreLabel token : document.tokens()) {
            String word = token.word().toLowerCase();
            if (word.matches(".*[a-zA-Z].*")) {
                totalWords++;
                uniqueWords.add(word);
            }
        }

        return totalWords == 0 ? 0.0 : (double) uniqueWords.size() / totalWords;
    }

    public static String findMostCommonCapitalizedWord(CoreDocument document) {
        Map<String, Integer> capitalizedWord = new LinkedHashMap<>();

        for (CoreSentence sentence : document.sentences()) {
            List<CoreLabel> tokens = sentence.tokens();

            for (int i = 1; i < tokens.size(); i++) {
                String word = tokens.get(i).word();

                if (word.matches(".*[a-zA-Z].*") && Character.isUpperCase(word.charAt(0))) {
                    capitalizedWord.put(word, capitalizedWord.getOrDefault(word, 0) + 1);
                }
            }
        }

        return getMaxCount(capitalizedWord);
    }

    public static Boolean determineUnitSystem(CoreDocument document) {
        int metricCount = 0;
        int imperialCount = 0;

        for (CoreLabel token : document.tokens()) {
            String word = token.word().toLowerCase();
            if (metricSet.contains(word)) {
                metricCount++;
            }
            if (imperialSet.contains(word)) {
                imperialCount++;
            }
        }

        if (metricCount > imperialCount) {
            return true;
        } else if (imperialCount > metricCount) {
            return false;
        } else {
            return null;
        }
    }

    private static class CurrencyMatch {
        String currency;
        int start;
        int end;

        CurrencyMatch(String currency, int start, int end) {
            this.currency = currency;
            this.start = start;
            this.end = end;
        }
    }

    public static String findMostCommonCurrency(CoreDocument document) {
        String text = document.text().toLowerCase();
        List<CurrencyMatch> matches = new ArrayList<>();
        Map<String, Integer> frequencyMap = new HashMap<>();

        List<String> sortedCurrencies = new ArrayList<>(currencySet);
        sortedCurrencies.sort((s1, s2) -> {
            int count1 = s1.split("\\s+").length;
            int count2 = s2.split("\\s+").length;
            return Integer.compare(count2, count1);
        });

        for (String currency : sortedCurrencies) {
            String patternString = "\\b" + Pattern.quote(currency.toLowerCase()) + "\\b";
            Pattern pattern = Pattern.compile(patternString);
            Matcher matcher = pattern.matcher(text);

            while (matcher.find()) {
                int start = matcher.start();
                int end = matcher.end();
                boolean overlap = false;
                for (CurrencyMatch m : matches) {
                    if (!(end <= m.start || start >= m.end)) {
                        overlap = true;
                        break;
                    }
                }
                if (!overlap) {
                    matches.add(new CurrencyMatch(currency, start, end));
                }
            }
        }

        for (CurrencyMatch cm : matches) {
            frequencyMap.put(cm.currency, frequencyMap.getOrDefault(cm.currency, 0) + 1);
        }

        String mostFrequent = null;
        int maxCount = 0;
        for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
            if (entry.getValue() > maxCount) {
                mostFrequent = entry.getKey();
                maxCount = entry.getValue();
            }
        }

        return mostFrequent;
    }

    public static String findMostCommonSurname(CoreDocument document) {
        Map<String, Integer> surnameCount = new LinkedHashMap<>();

        for (CoreLabel token : document.tokens()) {
            String ner = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
            String word = token.word().toLowerCase();

            if (surnameList.contains(word) || "PERSON".equals(ner)) {
                surnameCount.put(word, surnameCount.getOrDefault(word, 0) + 1);
            }
        }

        return getMaxCount(surnameCount);
    }

    public static String findMostCommonCountry(CoreDocument document) {
        Map<String, Integer> countryCount = new LinkedHashMap<>();

        for (CoreLabel token : document.tokens()) {
            String word = token.word().toLowerCase();

            if (countryList.contains(word)) {
                countryCount.put(word, countryCount.getOrDefault(word, 0) + 1);
            }
        }

        return getMaxCount(countryCount);
    }

    private static String getMaxCount(Map<String, Integer> count) {
        if (count.isEmpty()) return null;

        Map.Entry<String, Integer> max = count.entrySet().iterator().next();
        for (Map.Entry<String, Integer> entry : count.entrySet()) {
            if (entry.getValue() > max.getValue()) {
                max = entry;
            }
        }

        return max.getKey();
    }

    public static Integer countTotalSyllables(CoreDocument document) {
        char[] vowels = {'a', 'e', 'i', 'o', 'u', 'y'};
        int numVowels = 0;
        boolean lastWasVowel;
        String currentWord;

        for (CoreLabel token : document.tokens()) {
            currentWord = token.word().toLowerCase();
            lastWasVowel = false;

            for (char wc : currentWord.toCharArray()) {
                boolean foundVowel = false;
                for (char v : vowels) {// nie liczymy dyftongów
                    if (v == wc && lastWasVowel) {
                        foundVowel = true;
                        lastWasVowel = true;
                        break;
                    } else if (v == wc && !lastWasVowel) {
                        numVowels++;
                        foundVowel = true;
                        lastWasVowel = true;
                        break;
                    }
                }
                // jeśli cykl zakończył się i nie znaleziono samogłoski, ustawiamy lastWasVowel na false
                if (!foundVowel) {
                    lastWasVowel = false;
                }
            }

            // usuń "es" - zwykle jest nieme
            if (currentWord.length() > 2 && currentWord.endsWith("es")) {
                numVowels--;
            }
            // usuń nieme "e"
            else if (currentWord.length() > 1 && currentWord.endsWith("e")) {
                numVowels--;
            }
        }
        return numVowels;
    }

    public static Double calculateFleschReadingEase(CoreDocument document) {
        int sentenceCount = document.sentences().size();
        int syllableCount = countTotalSyllables(document);
        int wordCount = 0;

        for (CoreLabel token : document.tokens()) {
            String word = token.word().toLowerCase();
            if (word.matches(".*[a-zA-Z].*")) {
                wordCount++;
            }
        }
        return 206.835 - 1.015 * ((double) wordCount/sentenceCount) - 84.6 * ((double) syllableCount/wordCount);
    }

}
