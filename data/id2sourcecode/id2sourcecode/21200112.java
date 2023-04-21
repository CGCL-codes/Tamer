    boolean isOpped() {
        IRCConnection connection;
        connection = application.getConnection(connectionID);
        Channel channelObject;
        if (connection == null || channel == null) return false;
        channelObject = connection.getChannel(channel);
        if (channelObject == null) return false; else return channelObject.isOp(getNickName());
    }
