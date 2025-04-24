package org.example.extractor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.example.Article;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;

public class Serializer {
    static Gson gson;
    static {
        gson = new GsonBuilder().serializeNulls().create();
    }
    public static void saveArticlesToFile(List<String> fileNames) {
        List<Article> articles = SGMParser.convert(fileNames);

        for (Article article : articles) {
            FeatureExtractor.extractFeatures(article);
        }

        String json = gson.toJson(articles);
        try (FileWriter fileWriter = new FileWriter("articles.json")) {
            fileWriter.write(json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Article> readArticlesFromFile() {
        List<Article> articles;
        try (Reader reader = new FileReader("articles.json")) {
            Type articlesType = new TypeToken<List<Article>>(){}.getType();
            articles = gson.fromJson(reader, articlesType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return articles;
    }
}
