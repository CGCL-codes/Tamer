    public static InputStream getInputStream(Socket socket, long timeout) throws IOException {
        return (socket.getChannel() == null) ? socket.getInputStream() : new SocketInputStream(socket, timeout);
    }
