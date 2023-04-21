    private HashMap<String, ProcessedChartDataResults> processChartData(TreeMap<String, SourceChartDataResultSet> statresult) {
        final HashMap<String, ProcessedChartDataResults> results = new HashMap<String, ProcessedChartDataResults>(statresult.size());
        final HashMap<String, Source> sourceTable = ri.getSourceTable();
        Iterator<SourceChartDataResultSet> sourceIt = statresult.values().iterator();
        while (sourceIt.hasNext()) {
            final SourceChartDataResultSet scdrs = sourceIt.next();
            final String sourceName = scdrs.sourceName;
            final Source source = sourceTable.get(sourceName);
            if (source == null) {
                System.err.println("processChartData: invalid source: " + sourceName);
                continue;
            }
            Iterator<ChannelValueSet> channelIt = scdrs.channels.values().iterator();
            while (channelIt.hasNext()) {
                final ChannelValueSet values = channelIt.next();
                final String channelName = values.getChannelName();
                final List<Strip> channelStripMap = ri.getChannelStripList(channelName);
                if (channelStripMap == null) {
                    System.err.println("No strips for channel: " + channelName);
                    continue;
                }
                Iterator<Strip> stripIt = channelStripMap.iterator();
                while (stripIt.hasNext()) {
                    final Strip strip = stripIt.next();
                    final String key = sourceName + "|" + strip.getStripTitle();
                    ProcessedChartDataResults newResult = results.get(key);
                    if (newResult == null) {
                        final TimeDataSet newDataSet = new TimeDataSet();
                        setAxisMarkers(strip, newDataSet);
                        newResult = new ProcessedChartDataResults(source, strip, newDataSet);
                        results.put(key, newResult);
                    }
                    ChannelValueSet processedCVS = checkAggregators(strip.getDataAggregators(), values);
                    DataSeries2D series = new DataSeries2D(processedCVS.getTimeStamps(), processedCVS.getDataValues());
                    if (processedCVS.getType() == ChannelValueSet.AVGTYPE) {
                        series.setDrawLegend(false);
                        series.setAveraged(true);
                    }
                    series.setUniqueID(processedCVS.getChannelName());
                    series.setUnitType(processedCVS.getUnit());
                    series.setLabel(processedCVS.getLabel() + " [" + processedCVS.getUnit() + "]");
                    series.setPriority(processedCVS.getChannelPriority());
                    series.freshen();
                    newResult.addSeries(series);
                }
            }
        }
        return results;
    }
