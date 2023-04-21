    public int binomialQuantile(double x, int n, double p) {
        int k, k1, k2, del, ia;
        double m, s, p1, p2, pk;
        if (p == 0.0) return 0;
        if (p == n) return n;
        m = n * p;
        s = Math.sqrt(n * p * (1 - p));
        del = Math.max(1, (int) (0.2 * s));
        k = (int) (m + s * normalQuantile(x));
        k1 = k;
        k2 = k;
        do {
            k1 = k1 - del;
            k1 = Math.max(0, k1);
            p1 = binomialCDF(k1, n, p);
        } while (k1 > 0 && p1 > x);
        if (k1 == 0 && p1 >= x) return (k1);
        do {
            k2 = k2 + del;
            k2 = Math.min(n, k2);
            p2 = binomialCDF(k2, n, p);
        } while (k2 < n && p2 < x);
        if (k2 == n && p2 <= x) return (k2);
        while (k2 - k1 > 1) {
            k = (k1 + k2) / 2;
            pk = binomialCDF(k, n, p);
            if (pk < x) {
                k1 = k;
                p1 = pk;
            } else {
                k2 = k;
                p2 = pk;
            }
        }
        return (k2);
    }
