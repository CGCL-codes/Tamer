    private IChannel getChannel(String contextIdentity) {
        return getState().getChannels().get(contextIdentity);
    }
