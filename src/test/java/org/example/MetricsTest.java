package org.example;

import org.example.classifier.Metrics;
import org.junit.jupiter.api.Test;

class MetricsTest {

    @Test
    void generalizedNGramDistance() {
        String x = "eemm";
        String y = "emm";
        System.out.println(Metrics.generalizedNGramDistance(x, y));

    }
}