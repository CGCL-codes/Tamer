    public ClientGame(final GameData data, final Set<IGamePlayer> gamePlayers, final PlayerManager playerManager, final Messengers messengers) {
        m_data = data;
        m_playerManager = playerManager;
        if (m_playerManager == null) throw new IllegalArgumentException("Player manager cant be null");
        m_messengers = messengers;
        m_messenger = m_messengers.getMessenger();
        m_remoteMessenger = m_messengers.getRemoteMessenger();
        m_channelMessenger = m_messengers.getChannelMessenger();
        m_vault = new Vault(m_channelMessenger);
        m_channelMessenger.registerChannelSubscriber(m_gameModificationChannelListener, IGame.GAME_MODIFICATION_CHANNEL);
        m_remoteMessenger.registerRemote(m_gameStepAdvancer, getRemoteStepAdvancerName(m_channelMessenger.getLocalNode()));
        for (final IGamePlayer gp : gamePlayers) {
            final PlayerID player = m_data.getPlayerList().getPlayerID(gp.getName());
            m_gamePlayers.put(player, gp);
            final IPlayerBridge bridge = new DefaultPlayerBridge(this);
            gp.initialize(bridge, player);
            m_remoteMessenger.registerRemote(gp, ServerGame.getRemoteName(gp.getID(), data));
            final IRemoteRandom remoteRandom = new RemoteRandom(this);
            m_remoteMessenger.registerRemote(remoteRandom, ServerGame.getRemoteRandomName(player));
        }
        m_changePerformer = new ChangePerformer(m_data);
    }
