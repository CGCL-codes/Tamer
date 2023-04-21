    public void calcCurve() throws JuggleExceptionInternal {
        this.n = numpoints - 1;
        if (n < 1) throw new JuggleExceptionInternal("lineCurve error 1");
        this.a = new double[n][3];
        this.b = new double[n][3];
        this.durations = new double[n];
        for (int i = 0; i < n; i++) {
            durations[i] = times[i + 1] - times[i];
            if (durations[i] < 0.0) throw new JuggleExceptionInternal("lineCurve error 2");
        }
        double[] x = new double[n + 1];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < (n + 1); j++) x[j] = positions[j].getIndex(i);
            for (int j = 0; j < n; j++) {
                a[j][i] = x[j];
                b[j][i] = (x[j + 1] - x[j]) / durations[j];
            }
        }
    }
