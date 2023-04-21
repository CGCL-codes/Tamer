    private ServerChannel _getBayeuxServerChannel(final CometdPushChannel<?> pushChannel) {
        return _getBayeuxServer().getChannel(pushChannel.getCometdChannelId());
    }
