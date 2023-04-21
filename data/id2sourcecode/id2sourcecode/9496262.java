    private void setChannelSource(final ChannelSource channelSource) {
        synchronized (CHANNEL_LOCK) {
            final ChannelSource oldSource = _channelSource;
            _channelSource = null;
            if (oldSource != null) {
                final Channel oldChannel = oldSource.getChannel();
                if (oldChannel != null) {
                    oldChannel.removeConnectionListener(CONNECTION_HANDLER);
                }
            }
            stopMonitor();
            _channelSource = channelSource;
            if (channelSource != null) {
                final Channel channel = channelSource.getChannel();
                if (channel != null) {
                    channel.addConnectionListener(CONNECTION_HANDLER);
                    channel.requestConnection();
                    Channel.flushIO();
                }
            }
        }
        clear();
        EVENT_PROXY.channelSourceChanged(this, channelSource);
    }
