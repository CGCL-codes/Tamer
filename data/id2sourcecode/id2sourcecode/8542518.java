    protected List<DetailRecord> getChartData(DetailQuery query) throws IOException {
        final int numChannels = query.getChannelList().size();
        final PreparedStatement stmt = getChartDataStatement(query, query.getChannelList().size());
        final SourceInterface targetSource = query.getSource();
        final HashMap<String, TempSeriesHolder> channelnameToTempholder = new HashMap<String, TempSeriesHolder>();
        try {
            stmt.setString(1, targetSource.getName());
            stmt.setInt(2, (int) (query.getBeginDate().getTime() / 1000));
            stmt.setInt(3, (int) (query.getEndDate().getTime() / 1000));
            final List<InputChannelItemInterface> channelList = query.getChannelList();
            final int startOffset = 4;
            for (int i = 0; i < numChannels; i++) {
                stmt.setString(startOffset + i, channelList.get(i).getInternalName());
            }
            final ResultSet jdbcResult = readData(stmt);
            while (jdbcResult.next()) {
                final String key = jdbcResult.getString("key");
                TempSeriesHolder holder = channelnameToTempholder.get(key);
                if (holder == null) {
                    final String label = jdbcResult.getString("label");
                    final String unit = jdbcResult.getString("unit");
                    final InputChannelInterface channel = getChannelFromListByName(channelList, jdbcResult.getString("key"));
                    channelnameToTempholder.put(key, new TempSeriesHolder(channel, label, unit));
                } else {
                    holder.numVals++;
                }
            }
            for (Iterator<TempSeriesHolder> it = channelnameToTempholder.values().iterator(); it.hasNext(); ) {
                it.next().createTimeVals();
            }
            jdbcResult.beforeFirst();
            while (jdbcResult.next()) {
                final String key = jdbcResult.getString("key");
                TempSeriesHolder holder = channelnameToTempholder.get(key);
                if (holder == null) {
                    System.err.println("SwiftJDBCReader.getChartData: holder should never be null");
                } else {
                    holder.addTimeVal((double) jdbcResult.getInt("timeissued"), (double) jdbcResult.getFloat("val"));
                }
            }
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
        final List<DetailRecord> results = new ArrayList<DetailRecord>();
        final long queryId = query.getQueryId();
        for (Iterator<TempSeriesHolder> it = channelnameToTempholder.values().iterator(); it.hasNext(); ) {
            final TempSeriesHolder tempHolder = it.next();
            final SimpleDataSeries series = new SimpleDataSeries(tempHolder.times, tempHolder.vals);
            final DetailRecord record = new DetailRecord(targetSource, tempHolder.channel, series, queryId);
            results.add(record);
        }
        return results;
    }
