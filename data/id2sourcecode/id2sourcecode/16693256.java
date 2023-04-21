    @Override
    public int[] readDataFrame(int fr) {
        int[] tempret = new int[getDataLayout().getChannelCount()];
        for (int k = 0; k < tempret.length; k++) {
            tempret[k] = this.readDataPoint(k, fr);
        }
        return tempret;
    }
