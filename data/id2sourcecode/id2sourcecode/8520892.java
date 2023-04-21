    public static double[] intervalEstimationOfEX(MonitoredVar mv, double gamma) {
        double[] result = new double[2];
        int n, pion;
        int poz = 14;
        double t, przedz;
        double a = (1 + gamma) / 2;
        n = numberOfSamples(mv);
        if (n > 120) pion = 120; else pion = n - 1;
        if (a >= 0.9 && a < 1) poz = 0; else if (a >= 0.8 && a < 0.9) {
            poz = 1;
        } else if (a >= 0.7 && a < 0.8) {
            poz = 2;
        } else if ((a >= 0.6) && (a < 0.7)) {
            poz = 3;
        } else if ((a >= 0.5) && (a < 0.6)) {
            poz = 4;
        } else if ((a >= 0.4) && (a < 0.5)) {
            poz = 5;
        } else if ((a >= 0.3) && (a < 0.4)) {
            poz = 6;
        } else if ((a >= 0.2) && (a < 0.3)) {
            poz = 7;
        } else if ((a >= 0.1) && (a < 0.2)) {
            poz = 8;
        } else if ((a >= 0.05) && (a < 0.1)) {
            poz = 9;
        } else if ((a >= 0.04) && (a < 0.05)) {
            poz = 10;
        } else if ((a >= 0.03) && (a < 0.04)) {
            poz = 11;
        } else if ((a >= 0.02) && (a < 0.03)) {
            poz = 12;
        } else if ((a >= 0.01) && (a < 0.02)) {
            poz = 13;
        } else if ((a >= 0.001) && (a < 0.01)) {
            poz = 14;
        }
        t = tab_st[pion][poz];
        przedz = (standardDeviation(mv) * t) / java.lang.Math.sqrt(n);
        double sra = arithmeticMean(mv);
        result[0] = sra - przedz;
        result[1] = sra + przedz;
        return result;
    }
