    private void buildTimeSeriesCollection(HomenetHardware hw) {
        for (int channel = 0; channel < hw.getNumChannels(); channel++) {
            timeseriesCollection.addSeries(new TimeSeries(hw.getChannelDescription(channel) + " [CH-" + channel + "]", org.jfree.data.time.Second.class));
        }
    }
