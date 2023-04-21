    @Override
    public double[] readDataFrameAbsolute(int frame) {
        double tempret[] = new double[this.getDataLayout().getChannelCount()];
        for (int k = 0; k < tempret.length; k++) {
            tempret[k] = this.readDataPointAbsolute(k, frame);
        }
        return tempret;
    }
