    public JChannel getConnectedChannel() throws ChannelException {
        JChannel channel = getChannel();
        if (!channel.isConnected()) {
            log.debug("Connecting channel:" + channel);
            channel.connect(getClusterName());
        }
        return channel;
    }
