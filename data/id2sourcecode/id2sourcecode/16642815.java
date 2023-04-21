    public void addDataQuery(ChartDataQuery cdq) {
        sourceNames.add(cdq.sourceName);
        if (cdq.getAggType() < getAggType()) aggType = cdq.getAggType();
        if (!cdq.isGetAvg()) setGetAvg(false);
        if (startDate == null) startDate = cdq.startDate; else if (cdq.startDate != startDate) System.err.println("Illegal ChartDataAgg add, begin dates don't match.");
        if (endDate == null) endDate = cdq.endDate; else if (cdq.endDate != endDate) System.err.println("Illegal ChartDataAgg add, end dates don't match.");
        this.alarmRequestType = cdq.alarmRequestType;
        Strip strip = cdq.strip;
        if (strip.getStripTitle().equals(Strip.ALARMSTRIPTITLE)) {
            alarmQuery = true;
            return;
        }
        statQuery = true;
        Iterator<StripChannel> it = strip.getChannelList().iterator();
        while (it.hasNext()) {
            StripChannel channel = it.next();
            if (channel.getType() == StripChannel.STAT) channelNames.add("&sChannel=" + channel.getChannelID()); else if (channel.getType() == StripChannel.PREFIX) {
                List<StripChannel> channelList = strip.getChannelGroupList(channel);
                Iterator<StripChannel> channelListIt = channelList.iterator();
                while (channelListIt.hasNext()) {
                    final StripChannel c = channelListIt.next();
                    channelNames.add("&sChannel=" + c.getChannelID());
                }
            } else System.err.println("ChartDataAggQuery: invalid channel type: " + channel);
        }
    }
