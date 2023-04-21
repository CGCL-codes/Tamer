    public void broadcast(BroadcastInfo bi) {
        Channel channel = bi.getChannel();
        if (channel.hasListener()) {
            ChannelQueue channelQueue = ChannelQueue.getInstance();
            channelQueue.broadCast(bi);
        }
    }
