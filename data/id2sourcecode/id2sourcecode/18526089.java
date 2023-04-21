    public TreeMap<Date, Map<String, ChannelStats>> getChannelStatsForPeriod(Date start, Date end, String nodeId, int periodSizeInMinutes) {
        List<ChannelStats> list = sqlTemplate.query(getSql("selectChannelStatsSql"), new ChannelStatsMapper(), start, end, nodeId);
        return new ChannelStatsByPeriodMap(start, end, list, periodSizeInMinutes);
    }
