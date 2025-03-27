package org.example.featureExtractor;

import org.example.Article;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CurrencyCounter {
    public static String mostFrequentCurrency(Article article) {
        String text = article.getText();
        // Zbiory walut w wersji małych liter
        Set<String> currencies = new HashSet<>(Arrays.asList(
                "dollar", "austdlr", "hk", "singdlr", "nzdlr",
                "can", "stg", "dmk", "yen", "sfr",
                "ffr", "bfr", "dfl", "lit", "dkr",
                "nkr", "skr", "mexpeso", "cruzado",
                "austral", "saudriyal", "rand", "rupiah",
                "ringgit", "escudo", "peseta", "drachma"
        ));

        // Wyrażenie regularne do wyszukiwania jednostek walutowych
        Pattern pattern = Pattern.compile("\\b(?:DOLLAR|AUSTDLR|HK|SINGDLR|NZDLR|CAN|STG|DMK|YEN|SFR|FFR|BFR|DFL|LIT|DKR|NKR|SKR|MEXPESO|CRUZADO|AUSTRAL|SAUDRIYAL|RAND|RUPIAH|RINGGIT|ESCUDO|PESETA|DRACHMA)\\b", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);

        Map<String, Integer> currencyCount = new LinkedHashMap<>();
        List<String> foundCurrencies = new ArrayList<>();

        // Zliczanie walut, porównując bez względu na wielkość liter
        while (matcher.find()) {
            String currency = matcher.group().toLowerCase(); // Zamieniamy walutę na małe litery
            if (currencies.contains(currency)) {
                currencyCount.put(currency, currencyCount.getOrDefault(currency, 0) + 1);
                foundCurrencies.add(currency); // Zapisz pierwsze wystąpienie waluty
            }
        }

        // Sprawdzamy najczęściej występującą walutę
        String mostFrequentCurrency = null;
        int maxCount = 0;

        for (String currency : foundCurrencies) {
            int count = currencyCount.get(currency);
            if (count > maxCount) {
                mostFrequentCurrency = currency;
                maxCount = count;
            }
        }

        // Jeśli znaleziono walutę, zwróć ją w oryginalnej formie (pierwsza występująca)
        if (mostFrequentCurrency != null) {
            for (String currency : foundCurrencies) {
                if (currency.equalsIgnoreCase(mostFrequentCurrency)) {
                    return currency.toUpperCase(); // Zwracamy walutę w wersji dużych liter
                }
            }
        }

        return "Brak waluty";
    }
}
