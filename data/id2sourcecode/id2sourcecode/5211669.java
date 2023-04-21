    public ServerGame(final GameData data, final Set<IGamePlayer> localPlayers, final Map<String, INode> remotePlayerMapping, final Messengers messengers) {
        m_data = data;
        m_messenger = (IServerMessenger) messengers.getMessenger();
        m_remoteMessenger = messengers.getRemoteMessenger();
        m_channelMessenger = messengers.getChannelMessenger();
        m_vault = new Vault(m_channelMessenger);
        final Map<String, INode> allPlayers = new HashMap<String, INode>(remotePlayerMapping);
        for (final IGamePlayer player : localPlayers) {
            allPlayers.put(player.getName(), m_messenger.getLocalNode());
        }
        m_players = new PlayerManager(allPlayers);
        m_channelMessenger.registerChannelSubscriber(m_gameModifiedChannel, IGame.GAME_MODIFICATION_CHANNEL);
        CachedInstanceCenter.CachedGameData = data;
        setupLocalPlayers(localPlayers);
        setupDelegateMessaging(data);
        m_changePerformer = new ChangePerformer(data);
        m_randomStats = new RandomStats(m_remoteMessenger);
        m_remoteMessenger.registerRemote(m_serverRemote, SERVER_REMOTE);
    }
