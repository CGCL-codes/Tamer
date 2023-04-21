    public List<StripChannel> getChannelGroupList(StripChannel channel) {
        final HashMap<String, StripChannelGroup> groupMap = stripHandler.getChannelGroupMap();
        final StripChannelGroup scg = groupMap.get(channel.getChannelID());
        if (scg == null) {
            System.err.println("Could not find concrete StripChannels for virtual channel: " + channel);
            return new ArrayList<StripChannel>();
        }
        return scg.getChannelNames();
    }
