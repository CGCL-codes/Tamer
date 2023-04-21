    @Override
    public int[][] readData() {
        int[][] tempret = new int[getDataLayout().getChannelCount()][];
        for (int k = 0; k < tempret.length; k++) {
            tempret[k] = this.readDataTrace(k);
        }
        return tempret;
    }
