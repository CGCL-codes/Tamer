    @Override
    public SocketChannel detachChannel() throws IOException {
        final Socket sock = detachSocket();
        return (null == sock) ? null : sock.getChannel();
    }
