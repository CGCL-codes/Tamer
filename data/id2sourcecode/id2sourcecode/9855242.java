    public static double avgError(FloatBuffer a, FloatBuffer b, int dataSize) {
        double tot = 0;
        for (int i = 0; i < dataSize; i++) {
            float va = a.get(i), vb = b.get(i);
            float d = va - vb;
            if (Float.isNaN(d)) d = d + 0;
            if (d < 0) d = -d;
            float m = (va + vb) / 2;
            if (m == 0) continue;
            double r = d / (double) m;
            tot += r;
        }
        return tot / dataSize;
    }
