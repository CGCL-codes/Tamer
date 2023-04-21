    public void newGame() {
        m_serverMessenger.setAcceptNewConnections(true);
        final IClientChannel channel = (IClientChannel) m_channelMessenger.getChannelBroadcastor(IClientChannel.CHANNEL_NAME);
        notifyChanellPlayersChanged();
        channel.gameReset();
    }
