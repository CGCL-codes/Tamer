    public void diffuse() {
        int endX = xSize - 1;
        prevX = endX;
        x = 0;
        while (x < endX) {
            nextX = x + 1;
            computeColumn();
            prevX = x;
            x = nextX;
        }
        nextX = 0;
        computeColumn();
        writeMatrix.copyMatrixTo(readMatrix);
    }
