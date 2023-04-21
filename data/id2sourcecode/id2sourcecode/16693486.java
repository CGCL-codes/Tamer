    public void eval(double[] p) {
        double t = p[p.length - 1];
        int numPts = gi.getGroupSize();
        if (numPts > a.length) a = new double[2 * numPts];
        a[numPts - 1] = 1;
        double b = 1.0;
        double one_minus_t = 1.0 - t;
        for (int i = numPts - 2; i >= 0; i--) a[i] = a[i + 1] * one_minus_t;
        gi.set(0, 0);
        int i = 0;
        while (i < numPts) {
            double pt = PascalsTriangle.nCr(numPts - 1, i);
            if (Double.isInfinite(pt) || Double.isNaN(pt)) {
            } else {
                double gravity = a[i] * b * pt;
                double[] d = cp.getPoint(gi.next()).getLocation();
                for (int j = 0; j < p.length - 1; j++) p[j] = p[j] + d[j] * gravity;
            }
            b = b * t;
            i++;
        }
    }
