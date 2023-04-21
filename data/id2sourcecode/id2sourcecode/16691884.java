    private void addProbe(String channelName) {
        synchronized (channels) {
            if (!findChannel(channelName)) {
                final Channel channel = ChannelFactory.defaultFactory().getChannel(channelName);
                channel.addConnectionListener(connectionListener);
                channel.requestConnection();
                Channel.flushIO();
                channels.add(channel);
            }
        }
    }
