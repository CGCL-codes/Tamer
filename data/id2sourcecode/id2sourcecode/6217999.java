    public SocketInputStream(Socket socket) throws IOException {
        this(socket.getChannel(), socket.getSoTimeout());
    }
