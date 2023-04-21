    public static void addChannelListener(String channel, ChannelListener cl) {
        channelhandler.getChannel(channel).addListener(cl);
    }
