    static final HashMap<String, StripChannelGroup> createChannelGroupMap(List<StripChannel> stripChannels) {
        HashMap<String, StripChannelGroup> map = new HashMap<String, StripChannelGroup>();
        Iterator<StripChannel> it = stripChannels.iterator();
        while (it.hasNext()) {
            final StripChannel sc = it.next();
            final String channelId = sc.getChannelID();
            final String channelPrefix = getChannelPrefix(channelId);
            if (channelPrefix == null) continue;
            StripChannelGroup group = map.get(channelPrefix);
            if (group == null) {
                group = new StripChannelGroup(channelPrefix);
                map.put(channelPrefix, group);
            }
            group.addChannel(sc);
        }
        return map;
    }
