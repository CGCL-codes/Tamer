    private double[] getKnotArray(int uik, double[] orig, int nSegments) {
        double[] knots = new double[uik];
        if (isClosed) {
            int i, j;
            for (i = (this.degree - 1), j = (nSegments - 1); i >= 0; i--, j--) {
                knots[i] = knots[i + 1] - (orig[j + 1] - orig[j]);
            }
            for (i = (this.degree + 1), j = 1; j < (nSegments + 1); i++, j++) {
                knots[i] = orig[j];
            }
            for (j = 0; j < this.degree; i++, j++) {
                knots[i] = knots[i - 1] + (orig[j + 1] - orig[j]);
            }
        } else {
            for (int i = 0; i < knots.length; i++) {
                knots[i] = orig[i];
            }
        }
        return knots;
    }
