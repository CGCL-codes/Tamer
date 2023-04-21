    public boolean addProcessedDetailResults(DetailRecord resultSet, long updateNumber) {
        if (updateNumber != lastDetailQueryId) {
            bindingDataMap = new HashMap<StripChannelBinding, AbstractDrawableDataSeries>();
            this.lastDetailQueryId = updateNumber;
        }
        final StripChannelBinding binding = strip.getChannelBinding(resultSet.getInputChannelItem());
        final AbstractDrawableDataSeries drawableSeries = getStrip().makeDrawableSeries(resultSet.getSeries(), resultSet.getInputChannelItem());
        bindingDataMap.put(binding, drawableSeries);
        chartDataRequested = false;
        newData = true;
        checkCleanData();
        return true;
    }
