    public List getChannelsToRead() {
        return this.channelDAO.getChannels(IChannelDAO.CHANNELS_TO_READ);
    }
