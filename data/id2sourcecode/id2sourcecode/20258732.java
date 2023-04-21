    public HistorySynchronizer(final GameData data, final IGame game) {
        if (game.getData() == data) throw new IllegalStateException("You dont need a history synchronizer to synchronize game data that is managed by an IGame");
        m_data = data;
        m_data.forceChangesOnlyInSwingEventThread();
        data.acquireReadLock();
        try {
            m_currentRound = data.getSequence().getRound();
        } finally {
            data.releaseReadLock();
        }
        m_game = game;
        m_game.getChannelMessenger().registerChannelSubscriber(m_gameModifiedChannelListener, IGame.GAME_MODIFICATION_CHANNEL);
    }
