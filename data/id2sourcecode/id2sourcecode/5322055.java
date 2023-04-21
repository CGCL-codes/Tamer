    public Collection<Channel> getChannels() {
        Set<Channel> channels = new HashSet<Channel>(_channelWrappers.length);
        for (int index = 0; index < _channelWrappers.length; index++) {
            channels.add(_channelWrappers[index].getChannel());
        }
        return channels;
    }
