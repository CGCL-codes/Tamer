    @Override
    public void findWaveforms() throws RuntimeException {
        WaveformSnapshot snapshot = model.getRawWaveformSnapshot();
        double[] timeRange = snapshot.getTimeRange();
        _timeSettings.setLimits(timeRange[0], timeRange[1]);
        final Waveform[] waveforms = snapshot.getWaveforms();
        for (int index = 0; index < waveforms.length; index++) {
            final double epsilon = 1.0e-6;
            final double minValue = waveforms[index].getMinValue();
            final double maxValue = waveforms[index].getMaxValue();
            final double span = maxValue - minValue;
            String pvName = waveforms[index].getName();
            ChannelModel channelModel = model.getChannelModelWithPV(pvName);
            if (channelModel == null) continue;
            final double scale;
            final double offset;
            if (span < epsilon) {
                scale = 1.0;
                offset = -(minValue + maxValue) / 2;
            } else {
                scale = (UPPER_VALUE_LIMIT - LOWER_VALUE_LIMIT - 2) / span;
                offset = UPPER_VALUE_LIMIT - 1 - scale * maxValue;
            }
            channelModel.setSignalScale(scale);
            channelModel.setSignalOffset(offset);
        }
    }
