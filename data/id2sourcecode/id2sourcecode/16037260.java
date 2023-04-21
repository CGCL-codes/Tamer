    public TransformationMatrix(double[][] matrix, double translateX, double translateY) {
        fMatrix[0][0] = matrix[0][0];
        fMatrix[0][1] = matrix[0][1];
        fMatrix[1][0] = matrix[1][0];
        fMatrix[1][1] = matrix[1][1];
        fTranslateX = translateX;
        fTranslateY = translateY;
    }
