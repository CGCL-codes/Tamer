    public static int binomialQuantile(double d, int i, double d3) {
        if (d3 == 0.0D) return 0;
        if (d3 == (double) i) return i;
        double d4 = (double) i * d3;
        double d5 = Math.sqrt((double) i * d3 * (1.0D - d3));
        int j1 = Math.max(1, (int) (0.20000000000000001D * d5));
        int j = (int) (d4 + d5 * normalQuantile(d));
        int l = j;
        int i1 = j;
        double d6;
        do {
            l -= j1;
            l = Math.max(0, l);
            d6 = binomialCDF(l, i, d3);
        } while (l > 0 && d6 > d);
        if (l == 0 && d6 >= d) return l;
        double d8;
        do {
            i1 += j1;
            i1 = Math.min(i, i1);
            d8 = binomialCDF(i1, i, d3);
        } while (i1 < i && d8 < d);
        if (i1 == i && d8 <= d) return i1;
        while (i1 - l > 1) {
            int k = (l + i1) / 2;
            double d10 = binomialCDF(k, i, d3);
            if (d10 < d) {
                l = k;
                double d7 = d10;
            } else {
                i1 = k;
                double d9 = d10;
            }
        }
        return i1;
    }
