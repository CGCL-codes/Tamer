    public LobbyServer(final int port) {
        IServerMessenger server;
        try {
            server = new ServerMessenger(ADMIN_USERNAME, port);
        } catch (final IOException ex) {
            s_logger.log(Level.SEVERE, ex.toString());
            throw new IllegalStateException(ex.getMessage());
        }
        m_messengers = new Messengers(server);
        server.setLoginValidator(new LobbyLoginValidator());
        new ChatController(LOBBY_CHAT, m_messengers);
        final StatusManager statusManager = new StatusManager(m_messengers);
        statusManager.shutDown();
        new UserManager().register(m_messengers.getRemoteMessenger());
        new ModeratorController(server).register(m_messengers.getRemoteMessenger());
        final LobbyGameController controller = new LobbyGameController((ILobbyGameBroadcaster) m_messengers.getChannelMessenger().getChannelBroadcastor(ILobbyGameBroadcaster.GAME_BROADCASTER_CHANNEL), server);
        controller.register(m_messengers.getRemoteMessenger());
        server.setAcceptNewConnections(true);
    }
