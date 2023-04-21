    protected void applySignalToChannels(double[] rawData) {
        if (log.isDebugEnabled()) {
            log.debug("applySignalToChannels: Setting data:");
            for (double d : rawData) {
                log.debug("  " + d);
            }
        }
        double[] data = new double[rawData.length];
        for (int i = 0; i < data.length; i++) {
            data[i] = Math.abs(rawData[i]);
        }
        double[] processed;
        long millis = incrementMillisFromStart();
        if (log.isDebugEnabled()) {
            log.debug("for time from start " + millis);
        }
        for (EEGChannelState state : getChannelStates()) {
            processed = state.applyFilters(data);
            double rms = rootMeanSquare(processed);
            setChannelValue(getChannelValue(state.getFrequencyType()), state, rms, millis);
        }
    }
