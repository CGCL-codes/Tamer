    @Override
    public void connect() throws IOException {
        super.connect();
        if (socket == null) {
            socket = new Socket(address, port);
        }
        SocketChannel channel = socket.getChannel();
        setChannels(channel, channel);
    }
