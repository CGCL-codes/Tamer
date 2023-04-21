    public CellSwatchQuery(Date startDate, Date endDate, Iterator<SourceGroup> groupList, Iterator<Strip> stripIter, int alarmRequestType, int aggType) {
        this.aggType = aggType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.alarmRequestType = alarmRequestType;
        String baseQuery = getCoreQuery() + BASESWATCHQUERY;
        StringBuffer strbuf = new StringBuffer(baseQuery + "&beginTime=" + df.format(startDate) + "&endTime=" + df.format(endDate));
        while (groupList.hasNext()) {
            SourceGroup g = groupList.next();
            strbuf.append("&group=" + g.getType() + "|" + g.getShortname());
        }
        while (stripIter.hasNext()) {
            Strip strip = stripIter.next();
            try {
                StripChannel channel = strip.getFirstChannel();
                if (channel == null) {
                    System.err.println("No first channel for strip: " + strip);
                    continue;
                }
                if (channel.getType() == StripChannel.STAT) {
                    strbuf.append("&sChannel=" + channel.getChannelID());
                } else if (channel.getType() == StripChannel.PREFIX) {
                    List<StripChannel> channelList = strip.getChannelGroupList(channel);
                    Iterator<StripChannel> channelListIt = channelList.iterator();
                    while (channelListIt.hasNext()) {
                        final StripChannel c = channelListIt.next();
                        strbuf.append("&sChannel=" + c.getChannelID());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        queryString = strbuf.toString() + getAggregregate(aggType) + getOpenAlarmString();
    }
