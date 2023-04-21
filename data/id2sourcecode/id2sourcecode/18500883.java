    private double directionLessSobelEdgeDetectionValue(int xPos, int yPos) {
        int[][] d = new int[3][3];
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                d[x][y] = f(yPos + y - 1, xPos + x - 1);
            }
        }
        double sobelH = ((double) d[0][2] + (double) (2 * d[1][2]) + d[2][2] - d[0][0] - (2 * d[1][0]) - d[2][0]);
        double sobelV = ((double) d[2][0] + (double) (2 * d[2][1]) + d[2][2] - d[0][0] - (2 * d[0][1]) - d[0][2]);
        double sobelSum = (sobelH + sobelV) / 2;
        return sobelSum;
    }
