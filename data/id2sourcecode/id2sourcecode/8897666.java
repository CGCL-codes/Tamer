    }

    private static final int round2(int a, int b, int c, int d, int k, int s, int i, int[] x) {
        return b + rot((a + G(b, c, d) + x[k] + T[i - 1]), s);
    }

    private static final int round3(int a, int b, int c, int d, int k, int s, int i, int[] x) {
        return b + rot((a + H(b, c, d) + x[k] + T[i - 1]), s);
    }

