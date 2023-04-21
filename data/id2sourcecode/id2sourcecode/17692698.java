    private void updateLegend() {
        int series = dataCollection.getSeriesCount();
        int chans = channels.size();
        if (xyMode) {
            if (series == 0 && chans == 1) {
                String channelDisplay = getChannelDisplay((String) channels.get(0));
                domainAxis.setLabel(channelDisplay);
                rangeAxis.setLabel(null);
            } else if (series == 1 && chans == 0) {
                XYTimeSeries xySeries = ((XYTimeSeriesCollection) dataCollection).getSeries(0);
                String seriesName = (String) xySeries.getKey();
                String[] channelNames = seriesName.split(" vs. ");
                if (channelNames.length == 2) {
                    domainAxis.setLabel(channelNames[0]);
                    rangeAxis.setLabel(channelNames[1]);
                }
            } else if (series == 1 && chans == 2) {
                String channelDisplay1 = getChannelDisplay((String) channels.get(0));
                domainAxis.setLabel(channelDisplay1);
                String channelDisplay2 = getChannelDisplay((String) channels.get(1));
                rangeAxis.setLabel(channelDisplay2);
            } else {
                domainAxis.setLabel(null);
                rangeAxis.setLabel(null);
            }
        } else {
            if (series == 1) {
                String channelDisplay = getChannelDisplay((String) channels.get(0));
                rangeAxis.setLabel(channelDisplay);
            } else {
                rangeAxis.setLabel(null);
            }
        }
        if (showLegend && series >= 2) {
            if (chart.getLegend() == null) {
                chart.addLegend(seriesLegend);
            }
        } else {
            if (chart.getLegend() != null) {
                seriesLegend = chart.getLegend();
            }
            chart.removeLegend();
        }
    }
