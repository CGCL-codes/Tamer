    public void compute(final float p, final float[][] w) {
        w[0][0] = end[0][0];
        w[1][0] = end[1][0];
        w[2][0] = end[2][0];
        w[3][0] = end[3][0];
        w[4][0] = p * end[4][0] + (1 - p) * start[4][0];
        w[5][0] = p * end[5][0] + (1 - p) * start[5][0];
    }
