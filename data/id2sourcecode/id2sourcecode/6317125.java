    private void splint(double[] xa, double[] ya, double[] y2a, int n, double x, RefAny<Double> y) {
        int klo = 0;
        int khi = n - 1;
        int k;
        double _h;
        double a, b, yi;
        while ((khi - klo) > 1) {
            k = (khi + klo) / 2;
            if (xa[k] > x) {
                khi = k;
            } else {
                klo = k;
            }
        }
        _h = xa[khi] - xa[klo];
        if (_h == 0.0) {
            System.err.println("bad XA input to splint");
        }
        a = (xa[khi] - x) / _h;
        b = (x - xa[klo]) / _h;
        yi = a * ya[klo] + b * ya[khi] + ((a * a * a - a) * y2a[klo] + (b * b * b - b) * y2a[khi]) * _h * _h / 6.0;
        y.value = yi;
    }
