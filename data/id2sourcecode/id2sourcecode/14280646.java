    protected AgiChannel getChannel() {
        AgiChannel threadBoundChannel;
        if (channel != null) {
            return channel;
        }
        threadBoundChannel = AgiConnectionHandler.getChannel();
        if (threadBoundChannel == null) {
            throw new IllegalStateException("Trying to send command from an invalid thread");
        }
        return threadBoundChannel;
    }
