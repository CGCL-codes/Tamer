    public static final void iir_mem2(final float[] x, final int xs, final float[] den, final float[] y, final int ys, final int N, final int ord, final float[] mem) {
        int i, j;
        for (i = 0; i < N; i++) {
            y[ys + i] = x[xs + i] + mem[0];
            for (j = 0; j < ord - 1; j++) {
                mem[j] = mem[j + 1] - den[j + 1] * y[ys + i];
            }
            mem[ord - 1] = -den[ord] * y[ys + i];
        }
    }
