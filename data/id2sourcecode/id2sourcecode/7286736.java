    public SocketChannel getChannel() throws IOException {
        if (channel == null) {
            channel = SocketChannel.open();
            channel.configureBlocking(false);
            readDelegate.setChannel(channel);
        }
        return channel;
    }
