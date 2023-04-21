    public double[][] readDataVariable(int decimationFactor) {
        double[][] tempret = new double[getDataLayout().getChannelCount()][];
        for (int k = 0; k < tempret.length; k++) {
            tempret[k] = readDataTraceVariable(k, decimationFactor);
        }
        return tempret;
    }
