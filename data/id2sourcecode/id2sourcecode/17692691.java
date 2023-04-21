    protected void channelAdded(String channelName) {
        String channelDisplay = getChannelDisplay(channelName);
        String seriesName = null;
        Color color = null;
        if (xyMode) {
            if (channels.size() % 2 == 0) {
                String firstChannelName = (String) channels.get(channels.size() - 2);
                String firstChannelDisplay = getChannelDisplay(firstChannelName);
                seriesName = firstChannelDisplay + " vs. " + channelDisplay;
                color = getLeastUsedColor();
                XYTimeSeries data = new XYTimeSeries(seriesName, FixedMillisecond.class);
                data.setMaximumItemAge((long) (timeScale * 1000), (long) (time * 1000));
                int position = dataCollection.getSeriesCount() - localSeries;
                ((XYTimeSeriesCollection) dataCollection).addSeries(position, data);
            }
        } else {
            seriesName = channelDisplay;
            color = getLeastUsedColor();
            FastTimeSeries data = new FastTimeSeries(seriesName, FixedMillisecond.class);
            data.setMaximumItemAge((long) (timeScale * 1000), (long) (time * 1000));
            ((TimeSeriesCollection) dataCollection).addSeries(data);
        }
        if (seriesName != null) {
            colors.put(seriesName, color);
            setSeriesColors();
        }
        updateTitle();
        updateLegend();
    }
