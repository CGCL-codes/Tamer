    private void launchInNewThread(final Component parent) {
        if (m_inGameLobbyWatcher != null) {
            m_inGameLobbyWatcher.setGameStatus(GameDescription.GameStatus.LAUNCHING, null);
        }
        m_ui = parent;
        m_serverModel.setServerLauncher(this);
        s_logger.fine("Starting server");
        m_serverReady = new ServerReady(m_clientCount);
        m_remoteMessenger.registerRemote(m_serverReady, ClientModel.CLIENT_READY_CHANNEL);
        byte[] gameDataAsBytes;
        try {
            gameDataAsBytes = gameDataToBytes(m_gameData);
        } catch (final IOException e) {
            e.printStackTrace();
            throw new IllegalStateException(e.getMessage());
        }
        final Set<IGamePlayer> localPlayerSet = m_gameData.getGameLoader().createPlayers(m_localPlayerMapping);
        final Messengers messengers = new Messengers(m_messenger, m_remoteMessenger, m_channelMessenger);
        m_serverGame = new ServerGame(m_gameData, localPlayerSet, m_remotelPlayers, messengers);
        m_serverGame.setInGameLobbyWatcher(m_inGameLobbyWatcher);
        ((IClientChannel) m_channelMessenger.getChannelBroadcastor(IClientChannel.CHANNEL_NAME)).doneSelectingPlayers(gameDataAsBytes, m_serverGame.getPlayerManager().getPlayerMapping());
        final boolean useSecureRandomSource = !m_remotelPlayers.isEmpty() && !m_localPlayerMapping.isEmpty();
        if (useSecureRandomSource) {
            final PlayerID remotePlayer = m_serverGame.getPlayerManager().getRemoteOpponent(m_messenger.getLocalNode(), m_gameData);
            final CryptoRandomSource randomSource = new CryptoRandomSource(remotePlayer, m_serverGame);
            m_serverGame.setRandomSource(randomSource);
        }
        try {
            m_gameData.getGameLoader().startGame(m_serverGame, localPlayerSet);
        } catch (IllegalStateException e) {
            m_abortLaunch = true;
            Throwable error = e;
            while (error.getMessage() == null) error = error.getCause();
            final String message = error.getMessage();
            m_gameLoadingWindow.doneWait();
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    JOptionPane.showMessageDialog(null, message, "Warning", JOptionPane.WARNING_MESSAGE);
                }
            });
        } catch (final Exception e) {
            e.printStackTrace();
            m_abortLaunch = true;
        }
        m_serverReady.await();
        m_remoteMessenger.unregisterRemote(ClientModel.CLIENT_READY_CHANNEL);
        final Thread t = new Thread("Triplea, start server game") {

            @Override
            public void run() {
                try {
                    m_isLaunching = false;
                    if (!m_abortLaunch) {
                        if (useSecureRandomSource) {
                            warmUpCryptoRandomSource();
                        }
                        m_gameLoadingWindow.doneWait();
                        m_serverGame.startGame();
                    } else {
                        m_serverGame.stopGame();
                        SwingUtilities.invokeLater(new Runnable() {

                            public void run() {
                                JOptionPane.showMessageDialog(m_ui, "Error during startup, game aborted.");
                            }
                        });
                    }
                } catch (final MessengerException me) {
                    me.printStackTrace(System.out);
                    try {
                        if (!m_abortLaunch) m_erroLatch.await();
                    } catch (final InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                m_gameSelectorModel.loadDefaultGame(parent);
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        JOptionPane.getFrameForComponent(parent).setVisible(true);
                    }
                });
                m_serverModel.setServerLauncher(null);
                m_serverModel.newGame();
                if (m_inGameLobbyWatcher != null) {
                    m_inGameLobbyWatcher.setGameStatus(GameDescription.GameStatus.WAITING_FOR_PLAYERS, null);
                }
            }
        };
        t.start();
    }
