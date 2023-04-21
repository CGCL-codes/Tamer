    public InetSocketAddress getRemoteAddress() {
        Socket s = connection.getChannel().socket();
        InetAddress ia = s.getInetAddress();
        int port = s.getPort();
        return new InetSocketAddress(ia, port);
    }
