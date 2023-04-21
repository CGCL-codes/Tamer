    public RemoteChannel getChannel() {
        if (wrapper == null) throw new IllegalStateException("Socket instance not created yet!");
        return wrapper.getChannel();
    }
