    @Override
    public RemoteSocketChannel getChannel() {
        SocketChannel channel = socket.getChannel();
        if (channel instanceof RemoteSocketChannel) return (RemoteSocketChannel) channel;
        return new RemoteSocketChannelWrapper(channel);
    }
