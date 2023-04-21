    public void initialize(Socket socket) throws IOException, NotInitializedException {
        logger.debug("Initialising TCPTransportClient for a socket on [{}]", socket);
        socketDescription = socket.toString();
        socketChannel = socket.getChannel();
        socketChannel.configureBlocking(true);
        destAddress = new InetSocketAddress(socket.getInetAddress(), socket.getPort());
    }
