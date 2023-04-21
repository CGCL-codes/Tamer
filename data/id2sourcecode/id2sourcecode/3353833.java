    public static double[][] diagonal(double[][] matrix) {
        double[][] diagonal = new double[matrix.length][matrix[0].length];
        for (int i = 0; i < diagonal.length; i++) {
            diagonal[i][i] = matrix[i][i];
        }
        return diagonal;
    }
