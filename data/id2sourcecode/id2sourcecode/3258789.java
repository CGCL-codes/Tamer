    private static InputStream createSocketInputStream(Socket socket, int bufferSize) throws IOException {
        CheckArg.notNegative(bufferSize, "bufferSize");
        SocketChannel ch = socket.getChannel();
        if ((ch != null) && !ch.isBlocking()) ch.configureBlocking(true);
        InputStream in = socket.getInputStream();
        return (bufferSize > 0) ? new BufferedXInputStream(in, bufferSize) : in;
    }
