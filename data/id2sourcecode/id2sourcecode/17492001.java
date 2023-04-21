    protected ChannelFuture onRemoteConnected(ChannelFuture cf, HTTPRequestEvent event) {
        ChannelBuffer msg = buildRequestChannelBuffer(event);
        return cf.getChannel().write(msg);
    }
