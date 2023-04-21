    public static int getLineNumber(int position, int[] lineEnds, int g, int d) {
        if (lineEnds == null) return 1;
        if (d == -1) return 1;
        int m = g, start;
        while (g <= d) {
            m = g + (d - g) / 2;
            if (position < (start = lineEnds[m])) {
                d = m - 1;
            } else if (position > start) {
                g = m + 1;
            } else {
                return m + 1;
            }
        }
        if (position < lineEnds[m]) {
            return m + 1;
        }
        return m + 2;
    }
