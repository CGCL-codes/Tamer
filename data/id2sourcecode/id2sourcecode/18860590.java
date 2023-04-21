    public static double volumeSphere(double p, int t) {
        final double EPS = 2.0 * DBL_EPSILON;
        int pLR = (int) p;
        double kLR = (double) t;
        double Vol;
        int s;
        if (p < 0) throw new IllegalArgumentException("volumeSphere:   p < 0");
        if (Math.abs(p - pLR) <= EPS) {
            switch(pLR) {
                case 0:
                    return TWOEXP[t];
                case 1:
                    return TWOEXP[t] / (double) factorial(t);
                case 2:
                    if ((t % 2) == 0) return Math.pow(Math.PI, kLR / 2.0) / (double) factorial(t / 2); else {
                        s = (t + 1) / 2;
                        return Math.pow(Math.PI, (double) s - 1.0) * factorial(s) * TWOEXP[2 * s] / (double) factorial(2 * s);
                    }
                default:
            }
        }
        Vol = kLR * (LN2 + lnGamma(1.0 + 1.0 / p)) - lnGamma(1.0 + kLR / p);
        return Math.exp(Vol);
    }
