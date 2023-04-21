    public void broadcast(BroadcastInfo bi) {
        for (ChannelEntry channelEntry : listenerList) {
            Container container = channelEntry.getContainer();
            channelEntry.getChannelProgram().listen(container, this, bi);
        }
    }
