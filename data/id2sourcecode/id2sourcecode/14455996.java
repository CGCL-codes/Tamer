    public void diffuse() {
        int endY = ySize - 1;
        prevY = endY;
        y = 0;
        while (y < endY) {
            nextY = y + 1;
            computeRow();
            prevY = y;
            y = nextY;
        }
        nextY = 0;
        computeRow();
        writeMatrix.copyMatrixTo(readMatrix);
    }
