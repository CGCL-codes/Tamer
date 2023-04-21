    public void connectionTerminated(NetworkService networkBase, Channel connectedChannel) {
        if (connectedChannel.getConnectionState() == Channel.NETWORK_ERROR_CLOSING) {
            Log.logError("network", "NetworkSystemListenerImpl", "Unable to close connection for channel " + connectedChannel.getChannelNumber() + " (connection state: " + connectedChannel.getConnectionState() + ")");
        }
        Server server = Server.instance();
        UserRegistry userRegistry = server.getUserRegistry();
        User user = null;
        user = (User) connectedChannel.getCustomContextObject();
        if (user == null) {
            user = new User();
            user.setNetworkChannel(connectedChannel.getChannelNumber());
            user.setName("");
        }
        Log.log("network", "NetworkSystemListenerImpl", "Lost connection to user '" + user + "' (channel number: " + connectedChannel.getChannelNumber() + ")", Log.INFO);
        user.setOnline(false);
        user.setLoggedOutDate(new Date());
        ((ShutdownManager) Engine.instance().getManagerRegistry().getManager("shutdown")).shutdown(user);
        try {
            if (connectedChannel.getConnectionState() != Channel.NETWORK_ERROR_CLOSING) {
                networkBase.closeChannel(connectedChannel.getChannelNumber());
            }
        } catch (Exception x) {
        }
        Log.log("network", "NetworkSystemListenerImpl", "Cleaned up connection to user '" + user + "' (channel number: " + connectedChannel.getChannelNumber() + ")", Log.INFO);
    }
