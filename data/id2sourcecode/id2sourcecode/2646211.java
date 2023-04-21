    public LobbyFrame(final LobbyClient client, final LobbyServerProperties props) {
        super("TripleA Lobby");
        setIconImage(GameRunner.getGameIcon(this));
        m_client = client;
        setJMenuBar(new LobbyMenu(this));
        final Chat chat = new Chat(m_client.getMessenger(), LobbyServer.LOBBY_CHAT, m_client.getChannelMessenger(), m_client.getRemoteMessenger());
        m_chatMessagePanel = new ChatMessagePanel(chat);
        showServerMessage(props);
        m_chatMessagePanel.setShowTime(true);
        final ChatPlayerPanel chatPlayers = new ChatPlayerPanel(null);
        chatPlayers.addHiddenPlayerName(LobbyServer.ADMIN_USERNAME);
        chatPlayers.setChat(chat);
        chatPlayers.setPreferredSize(new Dimension(200, 600));
        chatPlayers.addActionFactory(new IPlayerActionFactory() {

            public List<Action> mouseOnPlayer(final INode clickedOn) {
                return createAdminActions(clickedOn);
            }
        });
        final LobbyGamePanel gamePanel = new LobbyGamePanel(m_client.getMessengers());
        final JSplitPane leftSplit = new JSplitPane();
        leftSplit.setOrientation(JSplitPane.VERTICAL_SPLIT);
        leftSplit.setTopComponent(gamePanel);
        leftSplit.setBottomComponent(m_chatMessagePanel);
        leftSplit.setResizeWeight(0.8);
        gamePanel.setPreferredSize(new Dimension(700, 200));
        m_chatMessagePanel.setPreferredSize(new Dimension(700, 400));
        final JSplitPane mainSplit = new JSplitPane();
        mainSplit.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        mainSplit.setLeftComponent(leftSplit);
        mainSplit.setRightComponent(chatPlayers);
        add(mainSplit, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        m_client.getMessenger().addErrorListener(new IMessengerErrorListener() {

            public void messengerInvalid(final IMessenger messenger, final Exception reason) {
                connectionToServerLost();
            }
        });
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(final WindowEvent e) {
                shutdown();
            }
        });
    }
