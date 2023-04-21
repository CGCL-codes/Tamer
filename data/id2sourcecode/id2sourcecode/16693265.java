    @Override
    public double[][] readDataAbsolute(int df) {
        double tempret[][] = new double[getDataLayout().getChannelCount()][];
        for (int k = 0; k < tempret.length; k++) {
            tempret[k] = readDataTraceAbsolute(k, df);
        }
        return tempret;
    }
