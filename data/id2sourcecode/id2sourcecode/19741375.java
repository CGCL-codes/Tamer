    public void connectionTerminated(NetworkService networkBase, Channel connectedChannel) {
        if (connectedChannel.getConnectionState() == (Channel.NETWORK_ERROR_CLOSING)) {
            Log.logError("network", "NetworkSystemListenerImpl.work", "Unable to close connection: " + connectedChannel.getConnectionState());
        }
        double channelNumber = connectedChannel.getChannelNumber();
        if (connectedChannel.getConnectionState() != Channel.NETWORK_ERROR_CLOSING) {
            networkBase.closeChannel(connectedChannel.getChannelNumber());
        }
        Client.instance().lostNetworkConnection();
        Log.log("network", "NetworkSystemListenerImpl.work", "Lost connection to server: " + connectedChannel.getConnectionState(), Log.INFO);
        return;
    }
