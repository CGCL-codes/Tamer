    public int poissonQuantile(double x, double l) {
        int k, k1, k2, del, ia;
        double m, s, p1, p2, pk;
        if (x == 0.0) return 0;
        if (l == 0.0) return 0;
        m = l;
        s = Math.sqrt(l);
        del = Math.max(1, (int) (0.2 * s));
        k = (int) (m + s * normalQuantile(x));
        k1 = k;
        k2 = k;
        do {
            k1 = k1 - del;
            k1 = Math.max(0, k1);
            p1 = poissonCDF(k1, l);
        } while (k1 > 0 && p1 > x);
        if (k1 == 0 && p1 >= x) return (k1);
        do {
            k2 = k2 + del;
            p2 = poissonCDF(k2, l);
        } while (p2 < x);
        while (k2 - k1 > 1) {
            k = (k1 + k2) / 2;
            pk = poissonCDF(k, l);
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
