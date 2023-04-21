    public NumericDataSample readSample() throws SAPIException {
        while (samples.isEmpty()) {
            if (time >= endTime) {
                return null;
            }
            fetchDataBlock();
        }
        Number[] data = new Number[channels.size()];
        for (int i = 0; i < channels.size(); i++) {
            data[i] = Double.NaN;
        }
        double t = samples.peek().getTime();
        while (!samples.isEmpty() && t == samples.peek().getTime()) {
            Sample sample = samples.remove();
            int channelIndex = channels.indexOf(sample.getChannel());
            data[channelIndex] = sample.getData();
        }
        NumericDataSample dataSample = new NumericDataSample(t, data);
        return dataSample;
    }
