    @Override
    protected void doDestroy() {
        getState().destroy();
        for (IChannel channel : getState().getChannels().values()) {
            try {
                channel.destroy();
            } catch (Exception e) {
                logException(getLog(), channel, e);
            }
        }
        getState().getChannels().clear();
    }
