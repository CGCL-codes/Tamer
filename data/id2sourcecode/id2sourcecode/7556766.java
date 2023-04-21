    private void removeChannel(Channel chan) {
        getChannels().remove(chan.getName());
        chan.removeChannelListener(getChannelMux());
        chan.getChannelMux().onDisconnect();
    }
