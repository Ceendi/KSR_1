package org.example;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.junit.jupiter.api.BeforeEach;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
class FeatureExtractorTest {
    private StanfordCoreNLP pipeline;
    CoreDocument document;
    @BeforeEach
    void setUp() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize");
        this.pipeline = new StanfordCoreNLP(props);
    }

    @org.junit.jupiter.api.Test
    void syllableTest() {
        document = new CoreDocument("kitty has a very large syllable");
        this.pipeline.annotate(document);

        assertEquals(9, FeatureExtractor.countTotalSyllables(document));

        document = new CoreDocument("Count syllables");
        this.pipeline.annotate(document);

        assertEquals(4, FeatureExtractor.countTotalSyllables(document));
    }
}