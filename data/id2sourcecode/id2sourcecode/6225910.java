    public void prepare() throws IOException {
        _selector = Selector.open();
        connectStartUpSet();
        ServerSocket socket = OhuaServerSocketFactory.getInstance().createServerSocket(_serverPort);
        _serverSockets.add(socket);
        socket.getChannel().register(_selector, SelectionKey.OP_ACCEPT);
    }
