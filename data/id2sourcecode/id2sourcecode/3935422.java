    private void composite(float[][] m) {
        float result[][] = new float[3][3];
        result[0][0] = matrix[0][0] * m[0][0] + matrix[0][1] * m[1][0] + matrix[0][2] * m[2][0];
        result[0][1] = matrix[0][0] * m[0][1] + matrix[0][1] * m[1][1] + matrix[0][2] * m[2][1];
        result[0][2] = matrix[0][0] * m[0][2] + matrix[0][1] * m[1][2] + matrix[0][2] * m[2][2];
        result[1][0] = matrix[1][0] * m[0][0] + matrix[1][1] * m[1][0] + matrix[1][2] * m[2][0];
        result[1][1] = matrix[1][0] * m[0][1] + matrix[1][1] * m[1][1] + matrix[1][2] * m[2][1];
        result[1][2] = matrix[1][0] * m[0][2] + matrix[1][1] * m[1][2] + matrix[1][2] * m[2][2];
        result[2][0] = matrix[2][0] * m[0][0] + matrix[2][1] * m[1][0] + matrix[2][2] * m[2][0];
        result[2][1] = matrix[2][0] * m[0][1] + matrix[2][1] * m[1][1] + matrix[2][2] * m[2][1];
        result[2][2] = matrix[2][0] * m[0][2] + matrix[2][1] * m[1][2] + matrix[2][2] * m[2][2];
        matrix[0][0] = result[0][0];
        matrix[0][1] = result[0][1];
        matrix[0][2] = result[0][2];
        matrix[1][0] = result[1][0];
        matrix[1][1] = result[1][1];
        matrix[1][2] = result[1][2];
        matrix[2][0] = result[2][0];
        matrix[2][1] = result[2][1];
        matrix[2][2] = result[2][2];
    }
