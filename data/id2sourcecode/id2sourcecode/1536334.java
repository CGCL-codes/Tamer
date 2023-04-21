    public void processPacket(IProtocolHandler handler_) {
        boolean allowed = handler_.isPortOpenAllowed(getHostname(), getPort());
        if (allowed) {
            OpenChannel channel = new OpenChannel(handler_, getHostname(), getPort(), getChannelNumber());
            Thread channelThread = new Thread(channel);
            channelThread.setDaemon(false);
            channelThread.start();
        } else {
            MSG_CHANNEL_OPEN_FAILURE reply = new MSG_CHANNEL_OPEN_FAILURE(getChannelNumber());
            handler_.enqueueToRemote(reply);
        }
    }
