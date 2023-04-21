    final void clearAllTimeseries() {
        List<Channel> channels = hardware.getChannels();
        for (Channel channel : channels) {
            TimeSeries ts = timeseriesCollection.getSeries(buildTimeSeriesKey(hardware, channel.getChannelNum()));
            ts.clear();
        }
    }
