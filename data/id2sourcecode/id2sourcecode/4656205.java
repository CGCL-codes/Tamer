    public InetSocketAddress getLocalAddress() {
        Socket s = connection.getChannel().socket();
        InetAddress ia = s.getLocalAddress();
        int port = s.getLocalPort();
        return new InetSocketAddress(ia, port);
    }
