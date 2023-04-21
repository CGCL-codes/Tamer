    public static void releaseExternalConnections() {
        for (Queue<ChannelFuture> conns : externalHostsToChannelFutures.values()) {
            for (ChannelFuture future : conns) {
                if (future.getChannel().isConnected()) {
                    future.getChannel().close();
                }
            }
        }
        externalHostsToChannelFutures.clear();
    }
