    public ChannelMap getChannelsMap() throws SAPIException {
        sink.RequestRegistration();
        return sink.Fetch(-1);
    }
