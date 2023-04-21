    public static double avgError(DoubleBuffer a, DoubleBuffer b, int dataSize) {
        double tot = 0;
        for (int i = 0; i < dataSize; i++) {
            double va = a.get(i), vb = b.get(i);
            double d = va - vb;
            if (Double.isNaN(d)) d = d + 0;
            if (d < 0) d = -d;
            double m = (va + vb) / 2;
            if (m == 0) continue;
            double r = d / (double) m;
            tot += r;
        }
        return tot / dataSize;
    }
