    public void channelRemoved(ChannelsEvent evt) {
        int channelNumber = evt.getChannelNumber();
        updateChannelsView(true, channelNumber);
    }
