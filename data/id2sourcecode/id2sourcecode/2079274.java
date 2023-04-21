    public Collection<Channel> getChannelsSP() {
        Set<Channel> channels = new HashSet<Channel>(_channelWrappersSP.length);
        for (int index = 0; index < _channelWrappersSP.length; index++) {
            channels.add(_channelWrappersSP[index].getChannel());
        }
        return channels;
    }
