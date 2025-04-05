package org.example;

public class Features {
    private final Double letterCount;
    private final Double avgWordLength;
    private final Double avgSentenceLength;
    private final Double uniqueWordRatio;
    private final String mostCommonSurname;
    private final String mostCommonCurrency;
    private final String mostCommonCountry;
    private final Boolean mostCommonUnitSystem;
    private final String mostFrequentCapitalized;
    private final Double FRE;

    public Features(Double letterCount, Double avgWordLength, Double avgSentenceLength, Double uniqueWordRatio,
                    String mostCommonSurname, String mostCommonCurrency, String mostCommonCountry,
                    Boolean mostCommonUnitSystem, String mostFrequentCapitalized, Double FRE) {
        this.letterCount = letterCount;
        this.avgWordLength = avgWordLength;
        this.avgSentenceLength = avgSentenceLength;
        this.uniqueWordRatio = uniqueWordRatio;
        this.mostCommonSurname = mostCommonSurname;
        this.mostCommonCurrency = mostCommonCurrency;
        this.mostCommonCountry = mostCommonCountry;
        this.mostCommonUnitSystem = mostCommonUnitSystem;
        this.mostFrequentCapitalized = mostFrequentCapitalized;
        this.FRE = FRE;
    }

    public Double getLetterCount() {
        return letterCount;
    }

    public Double getAvgWordLength() {
        return avgWordLength;
    }

    public Double getAvgSentenceLength() {
        return avgSentenceLength;
    }

    public Double getUniqueWordRatio() {
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

    public Double getFRE() {
        return FRE;
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
                ", mostCommonUnitSystem=" + mostCommonUnitSystem +
                ", mostFrequentCapitalized='" + mostFrequentCapitalized + '\'' +
                ", FRE=" + FRE +
                '}';
    }
}
