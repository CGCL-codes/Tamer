    public void gradient(double[] x, double[] g) {
        for (int i = 0; i < coefficients; i++) {
            xn[i] = x[i + 1];
            gn[i] = g[i + 1];
        }
        unit.gradient(xn, gn);
        for (int i = 0; i < coefficients; i++) {
            x[i + 1] = xn[i];
            g[i + 1] = gn[i];
        }
    }
