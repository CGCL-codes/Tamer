    public SocketChannel accept() throws IOException {
        if (!isOpen()) throw new ClosedChannelException();
        if (!serverSocket.isBound()) throw new NotYetBoundException();
        boolean completed = false;
        try {
            begin();
            serverSocket.getPlainSocketImpl().setInChannelOperation(true);
            NIOSocket socket = (NIOSocket) serverSocket.accept();
            completed = true;
            return socket.getChannel();
        } catch (SocketTimeoutException e) {
            return null;
        } finally {
            serverSocket.getPlainSocketImpl().setInChannelOperation(false);
            end(completed);
        }
    }
