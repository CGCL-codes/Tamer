    public SocketInputStream(Socket socket, long timeout) throws IOException {
        this(socket.getChannel(), timeout);
    }
