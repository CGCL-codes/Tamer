    private synchronized ChannelFuture getRemoteChannelFuture() {
        if (null == clientChannelFuture || (clientChannelFuture.isSuccess() && !clientChannelFuture.getChannel().isConnected())) {
            clientChannelFuture = connectProxyServer();
        }
        return clientChannelFuture;
    }
