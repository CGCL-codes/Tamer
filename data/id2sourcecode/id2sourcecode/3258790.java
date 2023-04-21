    private static OutputStream createSocketOutputStream(Socket socket) throws IOException {
        SocketChannel ch = socket.getChannel();
        if (ch != null) ch.configureBlocking(true);
        OutputStream out = socket.getOutputStream();
        return out;
    }
