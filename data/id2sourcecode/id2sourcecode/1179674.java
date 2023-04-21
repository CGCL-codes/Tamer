    public Channel getChatChannel() {
        return AppContext.getChannelManager().getChannel(chatPrefix + getName());
    }
