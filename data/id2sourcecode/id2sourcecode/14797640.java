    @Override
    public boolean isConnected(final InSimHost host) {
        final ConnectionInfo connInfo = fConnectionsInfo.get(host);
        final SocketChannel channel = (SocketChannel) connInfo.getChannel();
        if (channel == null) {
            return false;
        }
        return channel.isConnected();
    }
