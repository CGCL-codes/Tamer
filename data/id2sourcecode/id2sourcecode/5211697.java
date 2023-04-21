    private IGameModifiedChannel getGameModifiedBroadcaster() {
        return (IGameModifiedChannel) m_channelMessenger.getChannelBroadcastor(IGame.GAME_MODIFICATION_CHANNEL);
    }
