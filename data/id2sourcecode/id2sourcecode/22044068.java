    public List<Strip> getChannelStripList(final String channel) {
        HashSet<Strip> channelStripSet = this.channelStripMap.get(channel);
        final String channelPrefix = StripChannelGroup.getChannelPrefix(channel);
        HashSet<Strip> channelPrefixStripSet = null;
        if (channelPrefix != null) channelPrefixStripSet = this.channelStripMap.get(channelPrefix);
        final int totalSize = ((channelStripSet != null) ? channelStripSet.size() : 0) + ((channelPrefixStripSet != null) ? channelPrefixStripSet.size() : 0);
        ArrayList<Strip> returnList = new ArrayList<Strip>(totalSize);
        if (channelStripSet != null) returnList.addAll(channelStripSet);
        if (channelPrefixStripSet != null) returnList.addAll(channelPrefixStripSet);
        return returnList;
    }
