    public void startMonitor() {
        synchronized (CHANNEL_LOCK) {
            final ChannelSource channelSource = _channelSource;
            if (channelSource != null) {
                final Channel channel = channelSource.getChannel();
                if (channel.isConnected()) {
                    startMonitor(channel);
                }
            }
        }
    }
