    public static int poissonQuantile(double d, double d3) {
        if (d == 0.0D) return 0;
        if (d3 == 0.0D) return 0;
        double d4 = d3;
        double d5 = Math.sqrt(d3);
        int i1 = Math.max(1, (int) (0.20000000000000001D * d5));
        int i = (int) (d4 + d5 * normalQuantile(d));
        int k = i;
        int l = i;
        double d6;
        do {
            k -= i1;
            k = Math.max(0, k);
            d6 = poissonCDF(k, d3);
        } while (k > 0 && d6 > d);
        if (k == 0 && d6 >= d) return k;
        double d8;
        do {
            l += i1;
            d8 = poissonCDF(l, d3);
        } while (d8 < d);
        while (l - k > 1) {
            int j = (k + l) / 2;
            double d10 = poissonCDF(j, d3);
            if (d10 < d) {
                k = j;
                double d7 = d10;
            } else {
                l = j;
                double d9 = d10;
            }
        }
        return l;
    }
