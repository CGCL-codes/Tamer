    public void deactivate() {
        m_game.getChannelMessenger().unregisterChannelSubscriber(m_gameModifiedChannelListener, IGame.GAME_MODIFICATION_CHANNEL);
    }
