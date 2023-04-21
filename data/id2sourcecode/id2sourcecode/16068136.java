        public void doRun() {
            try {
                mes = reader.readMessage();
                Utilities.logParcial(Utilities.LOG_OUTPUT, "Message received from server: ");
                if (mes.getType() == Message.Type.ASK_TO_SPECTATE) {
                    Utilities.log(Utilities.LOG_OUTPUT, "Asking user to spectate.");
                    int response = swingSafe.showConfirmDialog("The lobby has reached the max amount of players.\n" + "Would you like to spectate?", "Max Players Reached", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
                    if (response == JOptionPane.YES_OPTION) {
                        Utilities.log(Utilities.LOG_OUTPUT, "User will spectate.");
                        settings.setObserving(true);
                        NetworkUtilities.sendMessage(writer, new Message(Message.Type.ASK_TO_SPECTATE, settings.getUserName(), true), false);
                        NetworkUtilities.sendMessage(writer, new Message(Message.Type.JOINED_LOBBY, settings.getUserName(), settings.getUserImage()), false);
                    } else {
                        Utilities.log(Utilities.LOG_OUTPUT, "User will not spectate, leaving lobby.");
                        swingSafe.createNewTableSelectWindow(settings, null);
                        swingSafe.leaveLobby(false, false);
                    }
                } else if (mes.getType() == Message.Type.CHAT) {
                    if (!hasFocus) {
                        newChat = true;
                    }
                    Utilities.log(Utilities.LOG_OUTPUT, "Chat from " + mes.getName());
                    Calendar today = Calendar.getInstance();
                    String meridiem;
                    if (today.get(Calendar.AM_PM) == Calendar.AM) {
                        meridiem = "AM";
                    } else {
                        meridiem = "PM";
                    }
                    String hour, minute, second;
                    hour = String.valueOf(today.get(Calendar.HOUR));
                    if (hour.equals("0")) {
                        hour = "12";
                    }
                    minute = String.valueOf(today.get(Calendar.MINUTE));
                    second = String.valueOf(today.get(Calendar.SECOND));
                    if (today.get(Calendar.MINUTE) < 10) {
                        minute = "0" + minute;
                    }
                    if (today.get(Calendar.SECOND) < 10) {
                        second = "0" + second;
                    }
                    String time = hour + ":" + minute + ":" + second + " " + meridiem;
                    swingSafe.appendToChat(mes.getName() + " (" + time + "): " + (String) mes.getContent());
                } else if (mes.getType() == Message.Type.CONNECTED) {
                    Utilities.log(Utilities.LOG_OUTPUT, "Connected");
                    reader.reInitialize("AES", Integer.MAX_VALUE, settings.getSecretKey());
                    writer = new EncryptedMessageWriter(lobbyWindow, new MessageWriter(socket.getOutputStream()), "RSA", 117, (PublicKey) mes.getContent());
                    NetworkUtilities.sendMessageAndWait(writer, new Message(Message.Type.REQUEST_HEADER, settings.getUserName(), settings.isObserving(), settings.getSecretKey()), true);
                    writer.reInitialize("AES", Integer.MAX_VALUE, settings.getSecretKey());
                    NetworkUtilities.sendMessage(writer, new Message(Message.Type.REQUEST_CLIENT_INFO, settings.getUserName(), "reqNames"), false);
                } else if (mes.getType() == Message.Type.CONNECTION_DENIED) {
                    Utilities.log(Utilities.LOG_OUTPUT, "Connection denied");
                    swingSafe.createNewTableSelectWindow(settings, new String[] { mes.getName(), "Couldn't Connect to Server" });
                    swingSafe.leaveLobby(false, false);
                } else if (mes.getType() == Message.Type.DELIVER_CLIENT_INFO) {
                    Serializable[] content = (Serializable[]) mes.getContent();
                    for (Serializable player : content) {
                        if (player != null) {
                            Serializable[] playerInfo = (Serializable[]) player;
                            swingSafe.addPlayer((String) playerInfo[0], (ImageIcon) playerInfo[1], (String) playerInfo[2]);
                        }
                    }
                    Utilities.log(Utilities.LOG_OUTPUT, "Client info delivered");
                    NetworkUtilities.sendMessage(writer, new Message(Message.Type.JOINED_LOBBY, settings.getUserName(), settings.getUserImage()), false);
                } else if (mes.getType() == Message.Type.JOINED_LOBBY) {
                    Utilities.log(Utilities.LOG_OUTPUT, "Member " + mes.getName() + " joined lobby");
                    Object[] content = (Object[]) mes.getContent();
                    swingSafe.addPlayer(mes.getName(), (ImageIcon) content[0], (String) content[1]);
                } else if (mes.getType() == Message.Type.KICK_USER) {
                    Utilities.log(Utilities.LOG_OUTPUT, "Kicked from lobby");
                    swingSafe.createNewTableSelectWindow(settings, new String[] { (String) mes.getContent(), "Kicked From Lobby" });
                    swingSafe.leaveLobby(false, true);
                } else if (mes.getType() == Message.Type.LEFT_LOBBY) {
                    Utilities.log(Utilities.LOG_OUTPUT, "Member " + mes.getName() + " left lobby");
                    swingSafe.removePlayer(mes.getName());
                } else if (mes.getType() == Message.Type.READY) {
                    boolean ready = ((String) mes.getContent()).equals("true");
                    Utilities.logParcial(Utilities.LOG_OUTPUT, mes.getName() + " ready: " + ready);
                    swingSafe.setPlayerReady(mes.getName(), ready);
                } else if (mes.getType() == Message.Type.ROOM_CLOSED) {
                    Utilities.log(Utilities.LOG_OUTPUT, "Room closed");
                    swingSafe.serverDcxn(EXPECTED);
                } else if (mes.getType() == Message.Type.ROOM_LOCKED) {
                    if (((String) mes.getContent()).equals("true")) {
                        Utilities.log(Utilities.LOG_OUTPUT, "Room locked");
                        if (waitForPlayers) {
                            swingSafe.allowReady(true);
                            swingSafe.setStatus("Waiting for players to be ready..." + "\n" + numOfReady + "/" + playerCount + " players ready.");
                        } else {
                            swingSafe.setStatus("Waiting for host to start game...");
                        }
                    } else {
                        Utilities.log(Utilities.LOG_OUTPUT, "Room unlocked");
                        swingSafe.allowReady(false);
                        if (swingSafe.getReadyButtonText().equals("Not Ready")) {
                            NetworkUtilities.sendMessage(writer, new Message(Message.Type.READY, settings.getUserName(), "false"), false);
                        }
                        swingSafe.setReadyButtonText("Ready");
                        swingSafe.setStatus("Waiting for more players...");
                    }
                } else if (mes.getType() == Message.Type.SERVER_INFO) {
                    Utilities.log(Utilities.LOG_OUTPUT, "Server info delivered");
                    swingSafe.setNameLabelText(mes.getName() + " Lobby");
                    waitForPlayers = (Boolean) mes.getContent();
                    if (waitForPlayers && !settings.isObserving()) {
                        swingSafe.setReadyButtonVisible(true);
                    }
                } else if (mes.getType() == Message.Type.STARTING_GAME) {
                    Utilities.log(Utilities.LOG_OUTPUT, "Starting game");
                    Serializable[] content = (Serializable[]) mes.getContent();
                    Player[] players = new Player[content.length];
                    for (int i = 0; i < players.length; i++) {
                        Serializable[] playerInfo = (Serializable[]) content[i];
                        players[i] = new Player((String) playerInfo[0], (ImageIcon) playerInfo[1], (Integer) playerInfo[2]);
                    }
                    swingSafe.createNewGameWindow(settings, players, reader, writer);
                    swingSafe.leaveLobby(false, false);
                } else if (mes.getType() == Message.Type.STATISTICS_UPDATE) {
                    Utilities.log(Utilities.LOG_OUTPUT, "Status update - " + mes.getContent());
                    swingSafe.setStatus((String) mes.getContent());
                } else if (mes.getType() == Message.Type.USERNAME_TAKEN) {
                    Utilities.log(Utilities.LOG_OUTPUT, "Username taken");
                    swingSafe.newUsername();
                } else {
                    Utilities.log(Utilities.LOG_OUTPUT, mes);
                }
            } catch (EMSCorruptedException emsce) {
                handleException(emsce);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
