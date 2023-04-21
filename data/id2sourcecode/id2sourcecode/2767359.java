    @SuppressWarnings("unchecked")
    public Collection<Channel> getChannels() {
        Set<Channel> result = new HashSet<Channel>();
        HashMap<String, ChannelImpl> channelsCopy = null;
        synchronized (channels) {
            channelsCopy = (HashMap<String, ChannelImpl>) channels.clone();
        }
        for (ChannelImpl channel : channelsCopy.values()) {
            result.add(channel);
        }
        return result;
    }
