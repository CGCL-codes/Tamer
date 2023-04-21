    public void calcCurve() throws JuggleExceptionInternal {
        int i, j;
        boolean edgeVelocitiesKnown = ((start_velocity != null) && (end_velocity != null));
        this.n = numpoints - 1;
        if (n < 1) throw new JuggleExceptionInternal("splineCurve error 1");
        this.a = new double[n][3];
        this.b = new double[n][3];
        this.c = new double[n][3];
        this.d = new double[n][3];
        this.durations = new double[n];
        for (i = 0; i < n; i++) {
            durations[i] = times[i + 1] - times[i];
            if (durations[i] < 0.0) throw new JuggleExceptionInternal("splineCurve error 2");
        }
        double[] x = new double[n + 1];
        double[] v = new double[n + 1];
        double t;
        for (i = 0; i < 3; i++) {
            for (j = 0; j < (n + 1); j++) x[j] = positions[j].getIndex(i);
            if (edgeVelocitiesKnown) {
                v[0] = start_velocity.getIndex(i);
                v[n] = end_velocity.getIndex(i);
                findvels_edges_known(v, x, durations, n, jugglinglab.core.Constants.SPLINE_LAYOUT_METHOD);
            } else {
                findvels_edges_unknown(v, x, durations, n, jugglinglab.core.Constants.SPLINE_LAYOUT_METHOD);
            }
            for (j = 0; j < n; j++) {
                a[j][i] = x[j];
                b[j][i] = v[j];
                t = durations[j];
                c[j][i] = (3.0 * (x[j + 1] - x[j]) - (v[j + 1] + 2.0 * v[j]) * t) / (t * t);
                d[j][i] = (-2.0 * (x[j + 1] - x[j]) + (v[j + 1] + v[j]) * t) / (t * t * t);
            }
        }
    }
