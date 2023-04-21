    private BsplineKnotVector knotDataClosed() {
        int degree = 3;
        int uik = (2 * degree) + uip + 1;
        double[] knots = new double[uik];
        int[] knotMultiplicities = new int[uik];
        knots[degree] = params[0];
        knotMultiplicities[degree] = 1;
        int i, j;
        for (i = degree - 1, j = uip - 1; i >= 0; i--, j--) {
            knots[i] = knots[i + 1] - pInt(j);
            knotMultiplicities[i] = 1;
        }
        for (i = degree + 1, j = 1; j < uip + 1; i++, j++) {
            knots[i] = params[j];
            knotMultiplicities[i] = 1;
        }
        for (j = 0; j < degree; i++, j++) {
            knots[i] = knots[i - 1] + pInt(j);
            knotMultiplicities[i] = 1;
        }
        return new BsplineKnotVector(3, KnotType.UNSPECIFIED, isClosed, uik, knotMultiplicities, knots, uip);
    }
