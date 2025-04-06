package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MetricsTest {

    @Test
    void generalizedNGramDistance() {
        String x = "eemm";
        String y = "emm";
        System.out.println(Metrics.generalizedNGramDistance(x, y));

    }
}