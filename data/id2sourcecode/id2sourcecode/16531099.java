    protected void splint(double[] xa, double[] ya, double[] y2a, int n, double x, doublestar y) {
        int klo = 0;
        int khi = n - 1;
        int k;
        double h;
        double a, b, yi;
        while ((khi - klo) > 1) {
            k = (khi + klo) / 2;
            if (xa[k] > x) khi = k; else klo = k;
        }
        h = xa[khi] - xa[klo];
        if (h == 0.0) System.out.println("bad XA input to splint");
        a = (xa[khi] - x) / h;
        b = (x - xa[klo]) / h;
        yi = a * ya[klo] + b * ya[khi] + ((a * a * a - a) * y2a[klo] + (b * b * b - b) * y2a[khi]) * h * h / 6.0;
        y.val = yi;
    }
