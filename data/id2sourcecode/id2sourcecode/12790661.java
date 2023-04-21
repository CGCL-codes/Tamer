    @Override
    public final int[] matrixToFrame(int[][] matrix) {
        assert matrix.length == 25 && matrix[0].length == 25 : "Incorrect matrix dimensions.";
        int[] tempret = new int[this.getChannelCount()];
        for (int ch = 0; ch < tempret.length; ch++) {
            int[] tempMXY = DET_TO_MAT_DATA[ch];
            tempret[ch] = matrix[tempMXY[0] - 1][tempMXY[1] - 1];
        }
        return tempret;
    }
