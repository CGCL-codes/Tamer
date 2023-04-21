    @Override
    public int[][] readData(int decimationFactor) {
        int[][] tempret = new int[getDataLayout().getChannelCount()][];
        for (int k = 0; k < tempret.length; k++) {
            tempret[k] = this.readDataTrace(k, decimationFactor);
        }
        return tempret;
    }
