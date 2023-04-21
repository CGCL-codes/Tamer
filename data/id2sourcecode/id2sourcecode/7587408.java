    public HashSet<ChannelFlag> getChannelFlags(int i) {
        if ((i >= 0) && (i < channelFlags.size())) return (HashSet<ChannelFlag>) channelFlags.elementAt(i);
        return emptyFlags;
    }
