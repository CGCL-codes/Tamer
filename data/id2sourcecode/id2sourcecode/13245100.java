    public void addToChannels(Channel channel) {
        Set<Channel> channels = getChannels();
        if (channels == null) {
            channels = new HashSet<Channel>();
            setChannels(channels);
        }
        channels.add(channel);
    }
