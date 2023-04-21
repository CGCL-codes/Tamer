    @Override
    public boolean isConnected(final InetSocketAddress hostAddress) {
        final ConnectionInfo connInfo = fConnectionsInfo.get(hostAddress);
        final SocketChannel channel = (SocketChannel) connInfo.getChannel();
        if (channel == null) {
            return false;
        }
        return channel.isConnected();
    }
