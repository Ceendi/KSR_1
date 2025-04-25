package org.example;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.example.extractor.FeatureExtractor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

    @Test
    void countTotalLetters() {
        document = new CoreDocument("to zdanie ma ponad trzydzieści liter");
        this.pipeline.annotate(document);
        assertEquals(31, FeatureExtractor.countTotalLetters(document));

        document = new CoreDocument("blep");
        this.pipeline.annotate(document);
        assertEquals(4, FeatureExtractor.countTotalLetters(document));
    }

    @Test
    void calculateAverageWordLength() {
        document = new CoreDocument("you have to be kidding mee");
        this.pipeline.annotate(document);
        assertEquals(3.5, FeatureExtractor.calculateAverageWordLength(document));

        document = new CoreDocument("a bb ccc dddd eeeee");
        this.pipeline.annotate(document);
        assertEquals(3, FeatureExtractor.calculateAverageWordLength(document));
    }

    @Test
    void calculateAverageSentenceLength() {
        document = new CoreDocument("Ja mam Cztery słowa. A ja mam pięć słów.");
        this.pipeline.annotate(document);
        assertEquals(4.5, FeatureExtractor.calculateAverageSentenceLength(document));

        document = new CoreDocument("Lovely Day. Beautiful sunny morning.");
        this.pipeline.annotate(document);
        assertEquals(2.5, FeatureExtractor.calculateAverageSentenceLength(document));
    }

    @Test
    void calculateUniqueWordRatio() {
        document = new CoreDocument("cat cat dog dog");
        this.pipeline.annotate(document);
        assertEquals(0.5, FeatureExtractor.calculateUniqueWordRatio(document));

        document = new CoreDocument("cat dog snake shark");
        this.pipeline.annotate(document);
        assertEquals(1, FeatureExtractor.calculateUniqueWordRatio(document));
    }

    @Test
    void findMostCommonCapitalizedWord() {
        document = new CoreDocument("Yes I love you. I love saying Yes");
        this.pipeline.annotate(document);
        assertEquals("I", FeatureExtractor.findMostCommonCapitalizedWord(document));

        document = new CoreDocument("Yes Yes No No");
        this.pipeline.annotate(document);
        assertEquals("No", FeatureExtractor.findMostCommonCapitalizedWord(document));
    }

    @Test
    void determineUnitSystem() {
        document = new CoreDocument("I have long feet. This field is 70 meters long");
        this.pipeline.annotate(document);
        assertEquals(0.5, FeatureExtractor.determineUnitSystem(document));

        document = new CoreDocument("gram meter foot pound inch");
        this.pipeline.annotate(document);
        assertEquals(0.6, FeatureExtractor.determineUnitSystem(document));
    }

    @Test
    void findMostCommonCurrency() {
        document = new CoreDocument("I paid 70 dollars for fake jordans");
        this.pipeline.annotate(document);
        assertEquals("dollars", FeatureExtractor.findMostCommonCurrency(document));

        document = new CoreDocument("canadian dollars");
        this.pipeline.annotate(document);
        assertEquals("canadian dollars", FeatureExtractor.findMostCommonCurrency(document));
    }

    @Test
    void findMostCommonSurname() {
        document = new CoreDocument("nakao, murdoch, nakao");
        this.pipeline.annotate(document);
        assertEquals("nakao", FeatureExtractor.findMostCommonSurname(document));
    }

    @Test
    void findMostCommonCountry() {
        document = new CoreDocument("american samoa guinea bissau");
        this.pipeline.annotate(document);
        assertEquals("american samoa", FeatureExtractor.findMostCommonCountry(document));

        document = new CoreDocument("canada, japan, japan.");
        this.pipeline.annotate(document);
        assertEquals("japan", FeatureExtractor.findMostCommonCountry(document));
    }

    @Test
    void countTotalSyllables() {
        document = new CoreDocument("kitty has a very large syllable");
        this.pipeline.annotate(document);

        assertEquals(9, FeatureExtractor.countTotalSyllables(document));

        document = new CoreDocument("Count syllables");
        this.pipeline.annotate(document);

        assertEquals(3, FeatureExtractor.countTotalSyllables(document));
    }

    @Test
    void calculateFleschReadingEase() {
        document = new CoreDocument("This is a very hard sentence");
        this.pipeline.annotate(document);
        assertEquals(87.94500000000002, FeatureExtractor.calculateFleschReadingEase(document));

        document = new CoreDocument("This sentence may be considered the hardest in the world.");
        this.pipeline.annotate(document);
        assertEquals(95.165, FeatureExtractor.calculateFleschReadingEase(document));
    }
}