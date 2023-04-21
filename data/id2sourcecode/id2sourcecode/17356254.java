    protected void computeCoefficients() throws ArithmeticException {
        int i, j, n;
        double c[], tc[], d, t;
        n = degree() + 1;
        coefficients = new double[n];
        for (i = 0; i < n; i++) {
            coefficients[i] = 0.0;
        }
        c = new double[n + 1];
        c[0] = 1.0;
        for (i = 0; i < n; i++) {
            for (j = i; j > 0; j--) {
                c[j] = c[j - 1] - c[j] * x[i];
            }
            c[0] *= (-x[i]);
            c[i + 1] = 1;
        }
        tc = new double[n];
        for (i = 0; i < n; i++) {
            d = 1;
            for (j = 0; j < n; j++) {
                if (i != j) {
                    d *= (x[i] - x[j]);
                }
            }
            if (d == 0.0) {
                throw new ArithmeticException("Identical abscissas cause division by zero.");
            }
            t = y[i] / d;
            tc[n - 1] = c[n];
            coefficients[n - 1] += t * tc[n - 1];
            for (j = n - 2; j >= 0; j--) {
                tc[j] = c[j + 1] + tc[j + 1] * x[i];
                coefficients[j] += t * tc[j];
            }
        }
        coefficientsComputed = true;
    }
