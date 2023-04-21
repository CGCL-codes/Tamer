    public double f(double t) {
        double period = x[n] - x[0];
        while (t > x[n]) t -= period;
        while (t < x[0]) t += period;
        int i = 0, j = n;
        while (i < j) {
            int k = (i + j) / 2;
            if (x[k] < t) i = k + 1; else j = k;
        }
        if (i > 0) i--;
        double h = x[i + 1] - x[i], d = t - x[i];
        return (((z[i + 1] - z[i]) * d / h + z[i] * 3) * d + ((y[i + 1] - y[i]) / h - (z[i] * 2 + z[i + 1]) * h)) * d + y[i];
    }
