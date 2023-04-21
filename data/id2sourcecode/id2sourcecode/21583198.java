    public void sendMessage(int channelId, String msg, be.trc.core.IRCServer server) {
        String channelName = getChannelName(channelId);
        ZorobotSystem.info(System.currentTimeMillis() + " SEND: " + channelName + ": " + msg);
        if (channelName != null) {
            sendMessage(channelName, msg, server);
        }
    }
