package org.example.featureExtractor;

import org.example.Article;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UnitCounter {
    public static String detectUnitSystem(Article article) {
        String text = article.getText();
        // Zbiory jednostek miary
        Set<String> metricUnits = new HashSet<>(Arrays.asList(
                "m", "km", "cm", "mm", "mg", "g", "kg", "ml", "l",
                "N", "kN", "Pa", "kPa", "MPa", "J", "kJ", "W", "kW", "MW"
        ));

        Set<String> imperialUnits = new HashSet<>(Arrays.asList(
                "inch", "foot", "yard", "mile", "oz", "lb", "gallon"
        ));

        // Wyrażenie regularne do wyszukiwania jednostek miary
        Pattern pattern = Pattern.compile("\\b(?:m|km|cm|mm|mg|g|kg|ml|l|N|kN|Pa|kPa|MPa|J|kJ|W|kW|MW|inch|foot|yard|mile|oz|lb|gallon)\\b");
        Matcher matcher = pattern.matcher(text);

        int metricCount = 0;
        int imperialCount = 0;

        // Zliczanie jednostek miary
        while (matcher.find()) {
            String unit = matcher.group().toLowerCase();
            if (metricUnits.contains(unit)) {
                metricCount++;
            } else if (imperialUnits.contains(unit)) {
                imperialCount++;
            }
        }

        // Określenie dominującego systemu jednostek
        if (metricCount > imperialCount) {
            return "System metryczny";
        } else if (imperialCount > metricCount) {
            return "System imperialny";
        } else {
            return "Brak dominującego systemu";
        }
    }
}
