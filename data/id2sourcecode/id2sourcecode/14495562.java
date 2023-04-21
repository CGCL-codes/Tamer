    private void plotDataOnEDT(final HardwareData data) {
        final Calendar dataDate = Calendar.getInstance();
        dataDate.setTime(data.getTimestamp());
        if (this.refreshPlotOnDataUpdate || (dataDate.after(plotStartDate) && dataDate.before(plotEndDate))) {
            TimeSeries ts = timeseriesCollection.getSeries(buildTimeSeriesKey(hardware, data.getChannel()));
            addDataInternal(ts, data);
        }
    }
