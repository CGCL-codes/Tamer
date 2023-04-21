    public Set<Channel> getChannelsWithoutMain() {
        Set<Channel> set = new HashSet<Channel>(getChannels());
        set.remove(getChannel());
        return set;
    }
