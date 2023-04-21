    public void disconnect() throws IOException {
        if (!connected) {
            throw new IllegalStateException("ClientSocket has not connected to the server yet");
        }
        unregisterKeys();
        getChannel().close();
        setChannel(null);
        connected = false;
    }
