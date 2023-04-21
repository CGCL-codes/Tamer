    public static OutputStream getOutputStream(Socket socket, long timeout) throws IOException {
        return (socket.getChannel() == null) ? socket.getOutputStream() : new SocketOutputStream(socket, timeout);
    }
