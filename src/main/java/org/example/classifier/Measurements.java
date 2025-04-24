package org.example.classifier;

public class Measurements {
    public static Double generalizedNGramDistance(String a, String b) {
        if (a == null || b == null) {
            return 0.0;
        }
        a = a.toLowerCase();
        b = b.toLowerCase();

        int minLength = Math.min(a.length(), b.length());
        int maxLength = Math.max(a.length(), b.length());
        int n_1 = 2;
        int n_2 = minLength;

        double fun = (double) ((maxLength - n_1+1)*(maxLength-n_1+2)-(maxLength-n_2)*(maxLength-n_2+1))/2;
        int correct = 0;
        for (int i = n_1; i <= n_2; i++) {
            for (int j = 1; j <= a.length() - i + 1; j++) {
                if (b.contains(a.substring(j-1, j - 1 + i))) {
                    correct++;
                }
            }
        }

        return (double) correct / fun;
    }
}
