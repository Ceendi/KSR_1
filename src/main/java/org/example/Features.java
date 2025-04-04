package org.example;

public class Features {
    private final int letterCount;
    private final double avgWordLength;
    private final double avgSentenceLength;
    private final double uniqueWordRatio;
    private final String mostCommonSurname;
    private final String mostCommonCurrency;
    private final String mostCommonCountry;
    private final Boolean mostCommonUnitSystem;
    private final String mostFrequentCapitalized;

    public Features(int letterCount, double avgWordLength, double avgSentenceLength, double uniqueWordRatio,
                    String mostCommonSurname, String mostCommonCurrency, String mostCommonCountry,
                    Boolean mostCommonUnitSystem, String mostFrequentCapitalized) {
        this.letterCount = letterCount;
        this.avgWordLength = avgWordLength;
        this.avgSentenceLength = avgSentenceLength;
        this.uniqueWordRatio = uniqueWordRatio;
        this.mostCommonSurname = mostCommonSurname;
        this.mostCommonCurrency = mostCommonCurrency;
        this.mostCommonCountry = mostCommonCountry;
        this.mostCommonUnitSystem = mostCommonUnitSystem;
        this.mostFrequentCapitalized = mostFrequentCapitalized;
    }

    public int getLetterCount() {
        return letterCount;
    }

    public double getAvgWordLength() {
        return avgWordLength;
    }

    public double getAvgSentenceLength() {
        return avgSentenceLength;
    }

    public double getUniqueWordRatio() {
        return uniqueWordRatio;
    }

    public String getMostCommonSurname() {
        return mostCommonSurname;
    }

    public String getMostCommonCurrency() {
        return mostCommonCurrency;
    }

    public String getMostCommonCountry() {
        return mostCommonCountry;
    }

    public Boolean getMostCommonUnitSystem() {
        return mostCommonUnitSystem;
    }

    public String getMostFrequentCapitalized() {
        return mostFrequentCapitalized;
    }

    @Override
    public String toString() {
        return "Features{" +
                "letterCount=" + letterCount +
                ", avgWordLength=" + avgWordLength +
                ", avgSentenceLength=" + avgSentenceLength +
                ", uniqueWordRatio=" + uniqueWordRatio +
                ", mostCommonSurname='" + mostCommonSurname + '\'' +
                ", mostCommonCurrency='" + mostCommonCurrency + '\'' +
                ", mostCommonCountry='" + mostCommonCountry + '\'' +
                ", mostCommonUnitSystem='" + mostCommonUnitSystem + '\'' +
                ", mostFrequentCapitalized='" + mostFrequentCapitalized + '\'' +
                '}';
    }
}
