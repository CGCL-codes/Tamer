    public static int search_in_array(double[] t, int from, int length, double value) {
        int a = from + 1;
        int b = length - 1;
        int m = 0;
        if (t[b] < value) return -1;
        if (value <= t[from]) return from;
        while (true) {
            if (b < a) {
                return -2;
            }
            m = (a + b) / 2;
            if (value <= t[m]) {
                if (value > t[m - 1]) return m;
                b = m - 1;
            } else a = m + 1;
        }
    }
