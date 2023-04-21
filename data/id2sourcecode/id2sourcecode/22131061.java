    public synchronized ChannelManager getChannelManager() {
        if (this.channelManager == null) {
            this.channelManager = new LoopbackChannelManager();
        }
        return this.channelManager;
    }
