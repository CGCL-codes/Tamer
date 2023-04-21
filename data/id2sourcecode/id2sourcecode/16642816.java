    @Override
    public String getQueryString() {
        final String baseQuery = getCoreQuery() + ChartDataQuery.getBASECHARTDATAQUERY();
        StringBuffer buf = new StringBuffer(baseQuery);
        buf.append("&beginTime=" + df.format(getStartDate()));
        buf.append("&endTime=" + df.format(getEndDate()));
        Iterator<String> sourceList = getSourceNames().iterator();
        while (sourceList.hasNext()) {
            buf.append("&source=" + sourceList.next());
        }
        Iterator<String> channelList = getChannelNames().iterator();
        while (channelList.hasNext()) {
            buf.append(channelList.next());
        }
        return buf.toString() + getAggregregate(aggType) + getOpenAlarmString() + getAvgString();
    }
