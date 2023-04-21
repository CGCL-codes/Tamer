    public Spline(double xx[], double ff[]) {
        double fp1, fpn, h, p;
        DecimalFormat f1 = new DecimalFormat("00.00000");
        boolean sorted = true;
        uniform = true;
        last_interval = 0;
        n = xx.length;
        if (n <= 3) throw new RuntimeException(Language.get("NOT_ENOUGH_POINTS_BUILD") + ", n=" + n);
        if (n != ff.length) throw new RuntimeException(Language.get("NOT_SAME_NUMBER"));
        x = new double[n];
        f = new double[n];
        b = new double[n];
        c = new double[n];
        d = new double[n];
        for (int i = 0; i < n; i++) {
            x[i] = xx[i];
            f[i] = ff[i];
            if (i >= 1 && x[i] < x[i - 1]) sorted = false;
        }
        if (!sorted) dHeapSort(x, f);
        b[0] = x[1] - x[0];
        c[0] = (f[1] - f[0]) / b[0];
        if (n == 2) {
            b[0] = c[0];
            c[0] = 0.0;
            d[0] = 0.0;
            b[1] = b[0];
            c[1] = 0.0;
            return;
        }
        d[0] = 2.0 * b[0];
        for (int i = 1; i < n - 1; i++) {
            b[i] = x[i + 1] - x[i];
            if (Math.abs(b[i] - b[0]) / b[0] > 1.0E-5) uniform = false;
            c[i] = (f[i + 1] - f[i]) / b[i];
            d[i] = 2.0 * (b[i] + b[i - 1]);
        }
        d[n - 1] = 2.0 * b[n - 2];
        fp1 = c[0] - b[0] * (c[1] - c[0]) / (b[0] + b[1]);
        if (n > 3) fp1 = fp1 + b[0] * ((b[0] + b[1]) * (c[2] - c[1]) / (b[1] + b[2]) - c[1] + c[0]) / (x[3] - x[0]);
        fpn = c[n - 2] + b[n - 2] * (c[n - 2] - c[n - 3]) / (b[n - 3] + b[n - 2]);
        if (n > 3) fpn = fpn + b[n - 2] * (c[n - 2] - c[n - 3] - (b[n - 3] + b[n - 2]) * (c[n - 3] - c[n - 4]) / (b[n - 3] + b[n - 4])) / (x[n - 1] - x[n - 4]);
        c[n - 1] = 3.0 * (fpn - c[n - 2]);
        for (int i = n - 2; i > 0; i--) c[i] = 3.0 * (c[i] - c[i - 1]);
        c[0] = 3.0 * (c[0] - fp1);
        for (int k = 1; k < n; k++) {
            p = b[k - 1] / d[k - 1];
            d[k] = d[k] - p * b[k - 1];
            c[k] = c[k] - p * c[k - 1];
        }
        c[n - 1] = c[n - 1] / d[n - 1];
        for (int k = n - 2; k >= 0; k--) c[k] = (c[k] - b[k] * c[k + 1]) / d[k];
        h = x[1] - x[0];
        for (int i = 0; i < n - 1; i++) {
            h = x[i + 1] - x[i];
            d[i] = (c[i + 1] - c[i]) / (3.0 * h);
            b[i] = (f[i + 1] - f[i]) / h - h * (c[i] + h * d[i]);
        }
        b[n - 1] = b[n - 2] + h * (2.0 * c[n - 2] + h * 3.0 * d[n - 2]);
    }
