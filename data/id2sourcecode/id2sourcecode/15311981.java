    private void calcGridVal(int i, int j, int k, double[][][] data, Gridcell grid) {
        grid.val[0] = data[i][j][k];
        grid.val[1] = data[i + 1][j][k];
        grid.val[2] = data[i + 1][j + 1][k];
        grid.val[3] = data[i][j + 1][k];
        grid.val[4] = data[i][j][k + 1];
        grid.val[5] = data[i + 1][j][k + 1];
        grid.val[6] = data[i + 1][j + 1][k + 1];
        grid.val[7] = data[i][j + 1][k + 1];
    }
