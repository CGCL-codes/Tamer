    public Channel getChannel() throws IOException {
        Channel channel = null;
        synchronized (_free) {
            if (!_free.isEmpty()) {
                channel = (Channel) _free.removeFirst();
            }
        }
        if (channel == null) {
            channel = open();
        }
        return channel;
    }
