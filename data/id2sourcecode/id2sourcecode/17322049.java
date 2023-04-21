    public void close() throws IOException {
        if (socket != null) {
            if (socket.isOpen()) {
                socket.socket().shutdownInput();
                socket.socket().shutdownOutput();
                socket.socket().close();
            }
        }
        WritableByteChannel wbc = getChannel();
        if (wbc != null) if (wbc.isOpen()) wbc.close();
        super.close();
    }
