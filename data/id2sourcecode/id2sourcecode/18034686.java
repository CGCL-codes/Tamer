    private void fillMessageMap() {
        messageCommands.put(MessageUserList.KEY, new MessageCommand() {

            @Override
            public void execute(Message message) {
                MessageUserList mul = (MessageUserList) message;
                users = mul.getUsers();
                usersByUserId.clear();
                usersByPlayerId.clear();
                for (UserInfo info : users) {
                    usersByUserId.put(info.getUserId(), info);
                    usersByPlayerId.put(info.getPlayerId(), info);
                }
                updatePlayerNames();
            }
        });
        messageCommands.put(MessageGameList.KEY, new MessageCommand() {

            @Override
            public void execute(Message message) {
                MessageGameList mgl = (MessageGameList) message;
                games = mgl.getGames();
                updateChannelList();
            }
        });
        messageCommands.put(MessageUserInGame.KEY, new MessageCommand() {

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
        });
        messageCommands.put(MessageSay.KEY, new MessageCommand() {

            @Override
            public void execute(Message message) {
                MessageSay ms = (MessageSay) message;
                int speakerId = ms.getSenderId();
                UserInfo speaker = usersByUserId.get(speakerId);
                if (speaker != null) {
                    int gameId = speaker.getPlayerId();
                    int relId = gameId % 6;
                    String cls = "p" + relId;
                    addOutputText("<span class='" + cls + "'>" + speaker.getName() + "</span>: " + HTMLEncode.encode(ms.getText()) + "<br>");
                } else {
                    addOutputText(ms.getText());
                }
            }
        });
        messageCommands.put(MessageUserInfo.KEY, new MessageCommand() {

            @Override
            public void execute(Message message) {
                MessageUserInfo mui = (MessageUserInfo) message;
                localUserInfo.updateFrom(mui.getUserInfo(), true);
                setStatus("Connected to server and authenticated. Multiplayer available.");
                authStatus = authStatus.success;
                doSendLater();
                if (channelId != -1) {
                    sendMessage(new MessageGameJoin(channelId));
                }
            }
        });
        messageCommands.put(MessageError.KEY, new MessageCommand() {

            @Override
            public void execute(Message message) {
                MessageError me = (MessageError) message;
                switch(Constants.ErrorType.valueOf(me.getErrorType())) {
                    case AuthFailed:
                        if (gameManager.getGameState().getGamePhase().getKey().equals(DefaultGamePhaseName.TITLE_SCREEN.name())) {
                            setStatus("Connected to server but not authenticated.");
                            authStatus = authStatus.failed;
                            mainGame.showAlert("You are not logged in on Usoog.com:\n" + "Only single player available.");
                        }
                        doSendLater();
                        break;
                    case ReplayLoadFailed:
                        if (gameManager.getGameState().getGamePhase().getKey().equals(DefaultGamePhaseName.LOAD_REPLAY.name())) {
                            setStatus("Failed to load replay!");
                            gameManager.getGameState().setGamePhase(DefaultGamePhaseName.TITLE_SCREEN.name());
                        }
                        break;
                }
            }
        });
        messageCommands.put(MessageSettings.KEY, new MessageCommand() {

            @Override
            public void execute(Message message) {
                MessageSettings mst = (MessageSettings) message;
                for (Entry<String, String> entry : mst.getSettings().entrySet()) {
                    settingKey key = Constants.settingKey.valueOf(entry.getKey());
                    switch(key) {
                        case ready:
                            UserInfo player = usersByUserId.get(mst.getSenderId());
                            if (player != null) {
                                player.setReady(Boolean.parseBoolean(entry.getValue()));
                            }
                            break;
                        case pause:
                            boolean paused = Boolean.parseBoolean(entry.getValue());
                            gameManager.setPause(paused, false);
                            break;
                        case ladderGame:
                        case openGame:
                        case publicGame:
                        case mapId:
                            gameManager.fireSettingChanged(key, entry.getValue());
                            break;
                        default:
                            System.out.println("PanelNetConnection::ExecuteMessageSettings: Unhandled setting: " + entry.getKey());
                            break;
                    }
                }
            }
        });
        messageCommands.put(MessagePong.KEY, new MessageCommand() {

            @Override
            public void execute(Message message) {
            }
        });
        messageCommands.put(MessageReplay.KEY, new MessageCommand() {

            @Override
            public void execute(Message message) {
                MessageReplay mrb = (MessageReplay) message;
                String key = gameManager.getGameState().getGamePhase().getKey();
                Logger.getLogger(PanelNetConnection.class.getName()).log(Level.INFO, "Got replay. Gamestate is {0}", key);
                if (key.equals(DefaultGamePhaseName.LOAD_REPLAY.name())) {
                    try {
                        gameManager.loadReplayLog(new BufferedReader(new StringReader(mrb.getReplay())), true);
                    } catch (FailedToLoadReplayException ex) {
                        Logger.getLogger(PanelNetConnection.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (key.equals(DefaultGamePhaseName.MP_JOIN_RUNNING.name()) || key.equals(DefaultGamePhaseName.MP_CATCHUP.name())) {
                    try {
                        gameManager.loadReplayLog(new BufferedReader(new StringReader(mrb.getReplay())), true);
                    } catch (FailedToLoadReplayException ex) {
                        Logger.getLogger(PanelNetConnection.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        messageCommands.put(MessageUnknown.KEY, new MessageCommand() {

            @Override
            public void execute(Message message) {
                try {
                    System.out.println("PanelNetConnection::textReceived: Unknown message:\n" + message.getMessage());
                } catch (Exception ex) {
                    Logger.getLogger(PanelNetConnection.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        messageCommands.put(MessageMapList.KEY, new MessageCommand() {

            @Override
            public void execute(Message message) {
                mapLoader.parseRemoteIndex((MessageMapList) message);
            }
        });
        messageCommands.put(MessageMapData.KEY, new MessageCommand() {

            @Override
            public void execute(Message message) {
                mapLoader.parseRemoteMap((MessageMapData) message);
            }
        });
        messageCommands.put(MessageMapLoad.KEY, new MessageCommand() {

            @Override
            public void execute(Message message) {
                MessageMapLoad mml = (MessageMapLoad) message;
                try {
                    mml.exec(gameManager);
                } catch (NotReadyYetException ex) {
                    Logger.getLogger(PanelNetConnection.class.getName()).log(Level.WARNING, "MessageMapLoad received, but map not ready yet.", ex);
                }
            }
        });
    }
