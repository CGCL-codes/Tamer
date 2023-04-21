    public Channel getChannel() {
        synchronized (this) {
            return _channel;
        }
    }
