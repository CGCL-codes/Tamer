    public Channel getUpdateChannel() {
        return AppContext.getChannelManager().getChannel(updatePrefix + localSpaceName);
    }
