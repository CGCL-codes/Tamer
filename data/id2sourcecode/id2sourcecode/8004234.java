    public Object getChannelManager() {
        this.getChannelManagerCalled = true;
        return getManager(this.channelManagerClazz);
    }
