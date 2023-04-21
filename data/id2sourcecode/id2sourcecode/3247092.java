    public double[][] readDataVariable() {
        double[][] tempret = new double[getDataLayout().getChannelCount()][];
        for (int k = 0; k < tempret.length; k++) {
            tempret[k] = readDataTraceVariable(k);
        }
        return tempret;
    }
