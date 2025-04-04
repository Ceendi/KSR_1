package org.example;

public class Article {
    private final String text;
    private final String label;
    private Features features;

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

    public void setFeatures(Features features) {
        this.features = features;
    }

    public Features getFeatures() {
        return features;
    }

    @Override
    public String toString() {
        return "Article{" +
                "text='" + text + '\'' +
                ", label='" + label + '\'' +
                ", features=" + features +
                '}';
    }
}
