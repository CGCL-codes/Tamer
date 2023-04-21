    protected void channelRemoved(String channelName) {
        if (!xyMode) {
            String channelDisplay = getChannelDisplay(channelName);
            TimeSeriesCollection dataCollection = (TimeSeriesCollection) this.dataCollection;
            TimeSeries data = dataCollection.getSeries(channelDisplay);
            dataCollection.removeSeries(data);
            colors.remove(channelDisplay);
        }
        setSeriesColors();
        updateTitle();
        updateLegend();
    }
