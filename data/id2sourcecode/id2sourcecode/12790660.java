    @Override
    public final int[][] frameToMatrix(int[] frame) {
        assert frame != null : "Cannot convert a null frame.";
        int tempMax = frame.length;
        assert tempMax < this.getDetectorCount() : "Invalid frame size.";
        if (tempMax >= this.getChannelCount()) {
            tempMax = this.getChannelCount();
        } else if (tempMax >= this.getDetectorCount()) {
            tempMax = this.getDetectorCount();
        }
        int[][] tempret = new int[25][25];
        for (int ch = 0; ch < tempMax; ch++) {
            int[] tempMXY = DET_TO_MAT_DATA[ch];
            tempret[tempMXY[0] - 1][tempMXY[1] - 1] = frame[ch];
        }
        return tempret;
    }
