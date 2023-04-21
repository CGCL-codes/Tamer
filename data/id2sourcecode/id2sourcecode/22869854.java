    @Override
    protected void serverRequest(QueryInterface req) throws IOException {
        final String interval = Integer.toString((int) ((TemporalQuery) req).getInterval().getMinutesPerSample());
        System.out.println("Selected interval is: " + interval);
        if (req instanceof SwatchQueryInterface) {
            final SwatchQueryInterface sreq = (SwatchQueryInterface) req;
            final List<SourceInterface> sources = sreq.getMergedSourceList(getSourceNameMap());
            final List<InputChannelItemInterface> channels = sreq.getInputChannelItemList();
            final List<SwatchRecordInterface> result = new ArrayList<SwatchRecordInterface>(sources.size() * channels.size());
            for (final SourceInterface source : sources) {
                for (final InputChannelItemInterface channel : channels) {
                    if (channel instanceof InputChannelInterface) {
                        final SwatchRecordInterface swatch = getSwatch(source, (InputChannelInterface) channel, sreq.getBeginDate(), sreq.getEndDate(), sreq.getQueryId(), interval);
                        if (swatch != null) result.add(swatch);
                    } else {
                        System.out.println("TODO CSV data dispatcher does not yet handle input channel groups");
                    }
                }
            }
            resultInterface.swatchQueryResult(result);
        } else if (req instanceof ChartDetailBulkQuery) {
            final ChartDetailBulkQuery creq = (ChartDetailBulkQuery) req;
            final List<SourceInterface> sources = creq.getSources();
            final List<InputChannelItemInterface> channels = creq.getChannelList();
            final List<DetailRecord> results = new ArrayList<DetailRecord>(sources.size() * channels.size());
            for (final SourceInterface source : sources) {
                for (final InputChannelItemInterface channel : channels) {
                    if (channel instanceof InputChannelInterface) {
                        final TimeSeriesData data = getDataFile(source.getInternalName(), channel.getInternalName(), interval).getTimeRange(creq.getBeginDate(), creq.getEndDate());
                        if (data == null) continue;
                        final double[] times = data.getTimestamps();
                        final double[] values = data.getValues();
                        final SimpleDataSeries series = new SimpleDataSeries(times, values);
                        final DetailRecord record = new DetailRecord(source, channel, series, creq.getQueryId());
                        results.add(record);
                    } else {
                        System.out.println("TODO CSV data dispatcher does not yet handle input channel groups");
                    }
                }
            }
            resultInterface.detailQueryResult(results);
        }
    }
