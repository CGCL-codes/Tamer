    public double[] readDataMinMaxAbsoluteInterval() {
        double[] tempRet = new double[this.getDataLayout().getChannelCount()];
        for (int k = 0; k < tempRet.length; k++) {
            tempRet[k] = readDataTraceMinMaxAbsoluteInterval(k);
        }
        return tempRet;
    }
