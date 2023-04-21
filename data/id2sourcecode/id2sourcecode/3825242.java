    public double getValue(double xval) {
        if (xval < x[0] || xval > x[n - 1]) {
            return Double.NaN;
        }
        if (xval < x[klo] || xval > x[khi]) {
            klo = 0;
            khi = n - 1;
        }
        while (khi - klo > 1) {
            int k = (khi + klo) / 2;
            if (x[k] > xval) {
                khi = k;
            } else {
                klo = k;
            }
        }
        double h = x[khi] - x[klo];
        double a = (x[khi] - xval) / h;
        double b = (xval - x[klo]) / h;
        return a * y[klo] + b * y[khi] + ((a * a * a - a) * y2[klo] + (b * b * b - b) * y2[khi]) * (h * h) / 6.0;
    }
