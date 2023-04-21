    public RemoteChannel getChannel() {
        if (socket != null) {
            SocketChannel socketChannel = socket.getChannel();
            if (socketChannel instanceof RemoteSocketChannel) return (RemoteSocketChannel) socketChannel;
            return new RemoteSocketChannelWrapper(socketChannel);
        } else {
            ServerSocketChannel serverSocketChannel = serverSocket.getChannel();
            if (serverSocketChannel instanceof RemoteServerSocketChannel) return (RemoteServerSocketChannel) serverSocketChannel;
            return new RemoteServerSocketChannelWrapper(serverSocketChannel);
        }
    }
