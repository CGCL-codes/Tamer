    private void addChannel(Channel chan) {
        getChannels().put(chan.getName(), chan);
        chan.addChannelListener(getChannelMux());
    }
