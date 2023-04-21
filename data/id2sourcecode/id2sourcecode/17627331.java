    public static final void fir_mem2(final float[] x, final int xs, final float[] num, final float[] y, final int ys, final int N, final int ord, final float[] mem) {
        int i, j;
        float xi;
        for (i = 0; i < N; i++) {
            xi = x[xs + i];
            y[ys + i] = num[0] * xi + mem[0];
            for (j = 0; j < ord - 1; j++) {
                mem[j] = mem[j + 1] + num[j + 1] * xi;
            }
            mem[ord - 1] = num[ord] * xi;
        }
    }
