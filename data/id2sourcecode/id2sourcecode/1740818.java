    @Override
    public void attach(Socket sock) throws IOException {
        if (null == sock) throw new SocketException("No " + Socket.class.getName() + " instance to attach");
        attach(sock.getChannel());
    }
