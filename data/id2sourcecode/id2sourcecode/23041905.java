    public Set getChannels() {
        TreeSet channels = new TreeSet(new ChannelDescComparitor());
        channels.addAll(m_channels.keySet());
        return channels;
    }
