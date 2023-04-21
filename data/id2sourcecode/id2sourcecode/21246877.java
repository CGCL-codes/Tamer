    public List<ScheduleByChannel> getSchedulesByChannel() {
        List<ScheduleByChannel> lsc = new ArrayList<ScheduleByChannel>();
        for (Iterator<URI> iterator = channelMap.iterator(); iterator.hasNext(); ) {
            Channel c = channelMap.get(iterator.next());
            lsc.add(getChannelSchedule(c));
        }
        return lsc;
    }
