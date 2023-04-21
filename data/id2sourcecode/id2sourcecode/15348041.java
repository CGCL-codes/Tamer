    private void solveTridiag(double[] sub, double[] diag, double[] sup, double[] f, int n) {
        int i;
        double a[] = new double[n];
        double b[] = new double[n];
        a[1] = -sub[0] / diag[0];
        b[1] = -f[0] / diag[0];
        for (i = 2; i < n; i++) {
            a[i] = -sub[i - 1] / (sup[i - 1] * a[i - 1] + diag[i - 1]);
            b[i] = (f[i - 1] - sup[i - 1] * b[i - 1]) / (sup[i - 1] * a[i - 1] + diag[i - 1]);
        }
        f[n - 1] = (f[n - 1] - sup[n - 1] * b[n - 1]) / (diag[n - 1] + sup[n - 1] * a[n - 1]);
        for (i = n - 2; i >= 0; i--) {
            f[i] = b[i + 1] - a[i + 1] * f[i + 1];
        }
    }
