package org.example;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Article {
    private final String text;
    private final String label;
    private final Map<String, Object> featureMap;

    public Article(Builder builder) {
        this.text = builder.text;
        this.label = builder.label;
        this.featureMap = new HashMap<>(builder.featureMap);
    }

    public String getText() {
        return text;
    }

    public String getLabel() {
        return label;
    }

    public Map<String, Object> getFeatureMap() {
        return Collections.unmodifiableMap(featureMap);
    }

    public void setFeature(String name, Object value) {
        featureMap.put(name, value);
    }

    @Override
    public String toString() {
        return "Article{" +
                "text='" + text + '\'' +
                ", label='" + label + '\'' +
                ", featureMap=" + featureMap +
                '}';
    }

    public static class Builder {
        private String text;
        private String label;
        private Map<String, Object> featureMap = new HashMap<>();

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Builder label(String label) {
            this.label = label;
            return this;
        }

        public Builder addFeature(String name, Object value) {
            this.featureMap.put(name, value);
            return this;
        }

        public Article build() {
            if (text == null || label == null) {
                throw new IllegalStateException("Article must have text, and label");
            }
            return new Article(this);
        }
    }
}
