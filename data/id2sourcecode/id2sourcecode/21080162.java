    private Vector driving(double[][] matrix, double[] vector) {
        int n = matrix.length - 1;
        double[] m = new double[n + 1];
        double[] k = new double[n + 1];
        m[1] = -matrix[0][1] / matrix[0][0];
        k[1] = vector[0] / matrix[0][0];
        for (int i = 1; i < n; i++) {
            m[i + 1] = -matrix[i][i + 1] / (matrix[i][i - 1] * m[i] + matrix[i][i]);
            k[i + 1] = (vector[i] - matrix[i][i - 1] * k[i]) / (matrix[i][i - 1] * m[i] + matrix[i][i]);
        }
        double[] y = new double[n + 1];
        y[n] = (vector[n] - matrix[n][n - 1] * k[n]) / (matrix[n][n - 1] * m[n] + matrix[n][n]);
        for (int i = n - 1; i >= 0; i--) {
            y[i] = m[i + 1] * y[i + 1] + k[i + 1];
        }
        return new Vector(y);
    }
