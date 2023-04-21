    private Channel getChannel(int channelId) throws IOException {
        Channel channel;
        Integer key = new Integer(channelId);
        synchronized (_channels) {
            channel = (Channel) _channels.get(key);
            if (channel == null) {
                throw new IOException("No channel exists with identifier: " + channelId);
            }
        }
        return channel;
    }
