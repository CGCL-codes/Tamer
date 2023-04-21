    public List getChannelsToPoll() {
        return channelDAO.getChannels(IChannelDAO.CHANNELS_TO_POLL);
    }
