    public SocketChannel getChannel(int i) {
        if (channels != null && 0 <= i && i < channels.length) {
            return channels[i];
        }
        return null;
    }
