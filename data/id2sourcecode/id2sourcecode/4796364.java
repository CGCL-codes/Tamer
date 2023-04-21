    public void prepareServer() throws IOException {
        _selector = Selector.open();
        ServerSocket socket = OhuaServerSocketFactory.getInstance().createServerSocket(1112);
        socket.getChannel().register(_selector, SelectionKey.OP_ACCEPT);
    }
