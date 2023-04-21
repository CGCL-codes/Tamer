    @SuppressWarnings("empty-statement")
    private double[] normalizeI(double[] I, int M) {
        int Mmin, Mmax;
        for (Mmin = 0; Mmin < M && I[Mmin] == 0; Mmin++) ;
        for (Mmax = M - 1; Mmax > 0 && I[Mmax] == 0; Mmax--) ;
        int Mmid = Mmin + (Mmax - Mmin) / 2;
        double mid = I[Mmid];
        if (mid == 0.0) {
            while (Mmid < Mmax && I[Mmid] == 0.0) {
                Mmid++;
            }
            mid = I[Mmid];
        }
        if (mid != 0.0) {
            for (int m = 0; m < M; m++) {
                I[m] /= mid;
            }
        }
        return I;
    }
