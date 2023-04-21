    public void hessian(double[] x, double[][] h) {
        System.out.println("asking for hessian");
        for (int i = 0; i < coefficients; i++) {
            xn[i] = x[i + 1];
            System.arraycopy(h[i + 1], 1, hn[i], 0, coefficients);
        }
        unit.hessian(xn, hn);
        for (int i = 0; i < coefficients; i++) {
            x[i + 1] = xn[i];
            System.arraycopy(hn[i], 0, h[i + 1], 1, coefficients);
        }
    }
