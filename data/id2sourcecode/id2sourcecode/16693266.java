    @Override
    public double[][] readDataAbsolute() {
        double tempret[][] = new double[this.getDataLayout().getChannelCount()][];
        for (int k = 0; k < tempret.length; k++) {
            tempret[k] = readDataTraceAbsolute(k);
        }
        return tempret;
    }
