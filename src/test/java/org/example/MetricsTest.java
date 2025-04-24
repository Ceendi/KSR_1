package org.example;

import org.example.classifier.Measurements;
import org.junit.jupiter.api.Test;

class MetricsTest {

    @Test
    void generalizedNGramDistance() {
        String x = "eemm";
        String y = "emm";
        System.out.println(Measurements.generalizedNGramDistance(x, y));

    }
}