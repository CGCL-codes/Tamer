    public BsplineKnotVector reverse() {
        if (knotSpec != KnotType.UNSPECIFIED) {
            return this;
        }
        int n_kel = nKnotValues();
        int[] new_multi = new int[n_kel];
        double[] new_knots = new double[n_kel];
        int i, j;
        new_multi[degree] = 1;
        new_knots[degree] = 0.0;
        int lk_idx = degree + nSegments();
        for (i = degree - 1, j = lk_idx; i >= 0; i--, j++) {
            new_knots[i] = new_knots[i + 1] - (knotValueAt(j + 1) - knotValueAt(j));
            new_multi[i] = 1;
        }
        for (i = degree + 1, j = lk_idx; i < n_kel; i++, j--) {
            new_knots[i] = new_knots[i - 1] + (knotValueAt(j) - knotValueAt(j - 1));
            new_multi[i] = 1;
        }
        return new BsplineKnotVector(degree, knotSpec, periodic, new_multi, new_knots, nControlPoints).beautify();
    }
