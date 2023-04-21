    public DelegateHistoryWriter(final IChannelMessenger messenger) {
        m_channel = (IGameModifiedChannel) messenger.getChannelBroadcastor(IGame.GAME_MODIFICATION_CHANNEL);
    }
