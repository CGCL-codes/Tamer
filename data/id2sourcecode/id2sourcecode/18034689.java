            @Override
            public void execute(Message message) {
                GameStateImplementation gameState = gameManager.getGameState();
                gameManager.getGameLoopManager().stop();
                MessageUserInGame muig = (MessageUserInGame) message;
                channelId = muig.getChannelId();
                playerGameId = muig.getPlayerId();
                gameManager.setLocalPlayerId(playerGameId);
                System.out.println("PanelNetConnection::textReceived: Channel: " + channelId + " Player: " + playerGameId);
                setChannelTitle();
                jTextField_send.setEnabled(true);
                jButton_send.setEnabled(true);
                if (channelId == 0) {
                    jPanelChannels.setVisible(true);
                    jPanelInLobby.setVisible(true);
                    jButtonExit.setVisible(false);
                    gameManager.getGameState().setGamePhase(DefaultGamePhaseName.MP_CONNECT.name());
                    jButtonKick.setVisible(false);
                    plr.setLongFormat(true);
                } else {
                    GameInfo info = gamesById.get(channelId);
                    if (info == null) {
                        channelName = "Unknown Channels";
                        Logger.getLogger(getClass().getName()).log(Level.WARNING, "Joined unknown channel with id{0}", channelId);
                    } else {
                        channelName = info.getName();
                    }
                    jPanelInLobby.setVisible(false);
                    jButtonExit.setVisible(true);
                    plr.setLongFormat(true);
                    if (muig.isRunning()) {
                        gameState.setGamePhase(DefaultGamePhaseName.MP_CATCHUP.name());
                        GliMpCatchup gl = (GliMpCatchup) gameState.getGamePhase().getGameLoop();
                        gl.setTargetTick(muig.getLastTick());
                    } else {
                        if (muig.getPlayerId() == 0) {
                            gameState.setGamePhase(DefaultGamePhaseName.MP_SELECTING.name());
                        } else {
                            gameState.setGamePhase(DefaultGamePhaseName.MP_CLIENT_SELECTING.name());
                        }
                    }
                    if (muig.getPlayerId() == 0) {
                        jButtonKick.setVisible(true);
                    } else {
                        jButtonKick.setVisible(false);
                    }
                }
            }
