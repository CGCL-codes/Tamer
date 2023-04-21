    private HashMap<String, ProcessedChartDataResults> processAlarmData(ChartDataAggQuery aggquery, Map<String, SourceAlarmDataResult> alarmresult) {
        HashMap<String, ProcessedChartDataResults> results = new HashMap<String, ProcessedChartDataResults>();
        final Strip alarmStrip = ri.getAlarmStrip();
        final HashMap<String, Source> sourceTable = ri.getSourceTable();
        final double[] queryTimeRange = getQueryTimeStamps(aggquery);
        Iterator<SourceAlarmDataResult> alarmResultIt = alarmresult.values().iterator();
        while (alarmResultIt.hasNext()) {
            final SourceAlarmDataResult sadr = alarmResultIt.next();
            final String sourceName = sadr.getSourceName();
            final Source source = sourceTable.get(sourceName);
            if (source == null) {
                System.err.println("ProcessAlarmData() invalid source: " + sourceName);
                continue;
            }
            List<AlarmRecord> alarms = sadr.getAlarmRecords();
            HashMap<String, TimeValuePair> alarmTimeValues = getAlarmTimeValues(alarms);
            Iterator<String> alarmTimeIterator = alarmTimeValues.keySet().iterator();
            while (alarmTimeIterator.hasNext()) {
                final String channelName = alarmTimeIterator.next();
                final TimeValuePair tvp = alarmTimeValues.get(channelName);
                final double[] alarmTimes = Util.toPrimitiveDouble(tvp.timeStamps);
                final double[] alarmValues = Util.toPrimitiveDouble(tvp.values);
                ChannelValueSet processedCVS = new ChannelValueSet(channelName, "", channelName, ChannelValueSet.ALARMTYPE);
                processedCVS.setTimeStamps(alarmTimes);
                processedCVS.setDataValues(alarmValues);
                processedCVS = checkAggregators(alarmStrip.getDataAggregators(), processedCVS, queryTimeRange);
                ProcessedChartDataResults result = results.get(sourceName);
                if (result == null) {
                    final DefaultDataSet newDataSet = new DefaultDataSet();
                    result = new ProcessedChartDataResults(source, alarmStrip, newDataSet);
                    results.put(sourceName, result);
                }
                AbstractDataSet dataSet = result.getDataSet();
                if (dataSet.getLabelSet(GLChart.X) == null) {
                    dataSet.setLabelSet(GLChart.X, buildCategoricalLabelSet(processedCVS));
                }
                DataSeries2D series = new DataSeries2D(processedCVS.getTimeStamps(), processedCVS.getDataValues());
                series.setUniqueID(processedCVS.getChannelName());
                series.setLabel(processedCVS.getLabel());
                series.freshen();
                result.addSeries(series);
            }
        }
        return results;
    }
