    public static double approx1(double v, double x[], double y[], int n, appr_meth Meth) {
        int i, j, ij;
        if (n == 0) return Double.NaN;
        i = 0;
        j = n - 1;
        if (v < x[i]) return Meth.ylow;
        if (v > x[j]) return Meth.yhigh;
        while (i < j - 1) {
            ij = (i + j) / 2;
            if (v < x[ij]) j = ij; else i = ij;
        }
        if (v == x[j]) return y[j];
        if (v == x[i]) return y[i];
        if (Meth.kind == 1) {
            return y[i] + (y[j] - y[i]) * ((v - x[i]) / (x[j] - x[i]));
        } else {
            return y[i] * Meth.f1 + y[j] * Meth.f2;
        }
    }
