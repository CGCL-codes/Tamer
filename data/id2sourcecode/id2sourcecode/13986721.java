    private void calculateCoefficients() {
        int N = pos1D.length;
        a = new double[N];
        b = new double[N];
        c = new double[N];
        d = new double[N];
        if (N == 2) {
            a[0] = pos1D[0];
            b[0] = pos1D[1] - pos1D[0];
            return;
        }
        double[] h = new double[N - 1];
        for (int i = 0; i < N - 1; i++) {
            a[i] = pos1D[i];
            h[i] = len[i + 1] - len[i];
            if (h[i] == 0.0) {
                h[i] = 0.01;
            }
        }
        a[N - 1] = pos1D[N - 1];
        double[][] A = new double[N - 2][N - 2];
        double[] y = new double[N - 2];
        for (int i = 0; i < N - 2; i++) {
            y[i] = 3 * ((pos1D[i + 2] - pos1D[i + 1]) / h[i + 1] - (pos1D[i + 1] - pos1D[i]) / h[i]);
            A[i][i] = 2 * (h[i] + h[i + 1]);
            if (i > 0) {
                A[i][i - 1] = h[i];
            }
            if (i < N - 3) {
                A[i][i + 1] = h[i + 1];
            }
        }
        solve(A, y);
        for (int i = 0; i < N - 2; i++) {
            c[i + 1] = y[i];
            b[i] = (a[i + 1] - a[i]) / h[i] - (2 * c[i] + c[i + 1]) / 3 * h[i];
            d[i] = (c[i + 1] - c[i]) / (3 * h[i]);
        }
        b[N - 2] = (a[N - 1] - a[N - 2]) / h[N - 2] - (2 * c[N - 2] + c[N - 1]) / 3 * h[N - 2];
        d[N - 2] = (c[N - 1] - c[N - 2]) / (3 * h[N - 2]);
    }
