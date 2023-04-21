    @Override
    public SocketChannel accept() throws IOException {
        try {
            return handler.getChannel();
        } catch (InterruptedException e) {
            throw new IOException(e);
        }
    }
