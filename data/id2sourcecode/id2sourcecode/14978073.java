    @Override
    public SocketChannel detachChannel() throws IOException {
        final SocketChannel retChannel = getChannel();
        setChannel(null);
        return retChannel;
    }
