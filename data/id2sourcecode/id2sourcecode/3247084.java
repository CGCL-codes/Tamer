    public double[][] readDataMinMaxAbsolute() {
        double[][] tempRet = new double[this.getDataLayout().getChannelCount()][2];
        for (int k = 0; k < tempRet.length; k++) {
            tempRet[k] = readDataTraceMinMaxAbsolute(k);
        }
        return tempRet;
    }
