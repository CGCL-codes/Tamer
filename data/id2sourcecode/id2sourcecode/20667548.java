    @Override
    public RemoteServerSocketChannel getChannel() {
        ServerSocketChannel serverSocketChannel = serverSocket.getChannel();
        if (serverSocketChannel instanceof RemoteServerSocketChannel) return (RemoteServerSocketChannel) serverSocketChannel;
        return new RemoteServerSocketChannelWrapper(serverSocketChannel);
    }
