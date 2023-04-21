    public static void addChannelListener(int id, ChannelListener cl) {
        channelhandler.getChannel(id).addListener(cl);
    }
