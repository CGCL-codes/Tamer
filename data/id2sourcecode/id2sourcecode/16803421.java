    public void handleAccept(NetworkRequest networkRequest) {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) networkRequest.getChannel();
        try {
            SocketChannel channel = serverSocketChannel.accept();
            networkEventThread.addChannelInterestOps(serverSocketChannel, SelectionKey.OP_ACCEPT);
            acceptServicer.accept(channel);
        } catch (IOException e) {
            log.error("Error accepting connection", e);
        }
    }
