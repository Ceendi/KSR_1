package org.example;

public class Article {
    private final String text;
    private final String label;

    public Article(String text, String label) {
        this.text = text;
        this.label = label;
    }

    public String getText() {
        return text;
    }

    public String getLabel() {
        return label;
    }
}
