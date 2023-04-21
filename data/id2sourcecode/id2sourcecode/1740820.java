    @Override
    public SocketChannel getChannel() {
        final Socket sock = getSocket();
        return (null == sock) ? null : sock.getChannel();
    }
