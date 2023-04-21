    public void addConnection(Socket socket) throws IOException {
        SocketChannel chanel = socket.getChannel();
        chanel.configureBlocking(false);
        chanel.register(this.connectionsSelector, SelectionKey.OP_READ);
    }
