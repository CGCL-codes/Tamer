    public void connect(SocketAddress endpoint, int timeout) throws IOException {
        if (isClosed()) {
            throw new SocketException("socket is closed");
        }
        if (!(endpoint instanceof LocalSocketAddress)) {
            throw new IllegalArgumentException("socket address is not a local address");
        }
        if (getChannel() != null && !getChannel().isBlocking()) {
            throw new IllegalBlockingModeException();
        }
        try {
            localimpl.doCreate();
            localimpl.localConnect((LocalSocketAddress) endpoint);
        } catch (IOException ioe) {
            close();
            throw ioe;
        }
        localConnected = true;
    }
