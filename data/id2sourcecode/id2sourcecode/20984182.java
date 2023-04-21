    public JChannel getChannel() throws ChannelException {
        if (channel == null) {
            channel = (JChannel) getChannelFactory().createChannel();
            channel.setOpt(Channel.AUTO_RECONNECT, true);
            channel.setOpt(Channel.AUTO_GETSTATE, true);
        }
        return channel;
    }
