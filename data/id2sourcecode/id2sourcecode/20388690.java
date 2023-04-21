    public boolean connect(InetSocketAddress serverAddress) {
        connection = new NetworkConnection(0);
        selector = null;
        readThread = null;
        writeThread = null;
        if (connection.connect(serverAddress)) {
            networkThreads();
            return true;
        } else {
            return false;
        }
    }
