        @Override
        public void incomingMessage(NetEvent event) {
            Socket socket = (Socket) event.getSource();
            DefaultData defaultData = event.getData();
            User user = userDatabase.getSocketOfUser(socket);
            if (defaultData instanceof UserData) {
                UserData userData = (UserData) defaultData;
                String oldUserName = user.getName();
                boolean rename = (oldUserName != null);
                boolean success = false;
                if (!rename) {
                    success = userDatabase.addUser(userData.getName(), socket);
                } else {
                    success = userDatabase.renameUser(user, userData.getName());
                    logger.log(Level.INFO, "Renamed of {0} to {1} was {2}", new Object[] { oldUserName, userData.getName(), success ? "successful!" : "unsuccessful!" });
                }
                ServerMessage serverMessage = new ServerMessage();
                if (success) {
                    if (!rename) {
                        UserConnection userConnection = new UserConnection();
                        userConnection.setUser(user);
                        userConnection.setConnected(true);
                        ArrayList<Socket> userSocketList = (ArrayList<Socket>) userDatabase.getSocketList();
                        sendToAllSockets(userSocketList, userConnection);
                    } else {
                        channelDatabase.renameUser(oldUserName, userData.getName());
                    }
                    serverMessage.setMessageEnum(ServerMessageEnum.USERNAME_SET);
                    serverMessage.setAdditionalMessage(userData.getName());
                } else {
                    serverMessage.setMessageEnum(ServerMessageEnum.ERROR_USERNAME_IN_USE);
                    serverMessage.setAdditionalMessage(userData.getName());
                }
                tcpSocket.writeData(socket, serverMessage);
            } else if (defaultData instanceof Ping) {
                Pong pong = new Pong();
                pong.setTimestamp(((Ping) defaultData).getTimestamp());
                tcpSocket.writeData(socket, pong);
            } else if (defaultData instanceof ChatMessage) {
                ChatMessage chatMessage = ((ChatMessage) defaultData);
                chatMessage.setFromUser(user);
                if (chatMessage.isChannel()) {
                    ArrayList<Socket> userSocketList = (ArrayList<Socket>) channelDatabase.getSocketsOfUsersInChannel(chatMessage.getTo(), chatMessage.getFromUser());
                    if (userSocketList != null) {
                        for (Socket userSocket : userSocketList) {
                            tcpSocket.writeData(userSocket, chatMessage);
                        }
                    } else {
                        ServerMessage serverMessage = new ServerMessage();
                        serverMessage.setMessageEnum(ServerMessageEnum.ERROR_CHANNEL_NON_EXISTENT);
                        serverMessage.setAdditionalMessage(chatMessage.getTo());
                        tcpSocket.writeData(socket, serverMessage);
                    }
                } else {
                    Socket toUserSocket = userDatabase.getUserOfSocket(chatMessage.getTo());
                    if (toUserSocket != null) {
                        tcpSocket.writeData(toUserSocket, chatMessage);
                    } else {
                        ServerMessage serverMessage = new ServerMessage();
                        serverMessage.setMessageEnum(ServerMessageEnum.ERROR_USER_NON_EXISTENT);
                        serverMessage.setAdditionalMessage(chatMessage.getTo());
                        tcpSocket.writeData(socket, serverMessage);
                    }
                }
            } else if (defaultData instanceof UserList) {
                UserList userList = ((UserList) defaultData);
                String channel = userList.getChannel();
                if (channel != null) {
                    userList.setUserList(channelDatabase.getUsersInChannel(channel));
                } else {
                    userList.setUserList(userDatabase.getUserList());
                }
                tcpSocket.writeData(socket, userList);
            } else if (defaultData instanceof UserConnection) {
                UserConnection userConnection = ((UserConnection) defaultData);
                if (!userConnection.isConnected()) {
                    boolean success = userDatabase.removeUser(userConnection.getUser());
                    ArrayList<String> userChannelList = (ArrayList<String>) channelDatabase.getUserChannels(userConnection.getUser());
                    channelDatabase.removeUserFromAllChannels(userConnection.getUser());
                    for (String channel : userChannelList) {
                        if (!channelDatabase.channelExists(channel)) {
                            notifyChannelStatusChange(channel, false);
                        }
                    }
                }
                ArrayList<Socket> userSocketList = (ArrayList<Socket>) userDatabase.getSocketList();
                sendToAllSockets(userSocketList, userConnection);
                logger.info(userConnection.toString());
            } else if (defaultData instanceof ChannelList) {
                ChannelList channelList = ((ChannelList) defaultData);
                channelList.setChannelList(channelDatabase.getListOfChannels());
                tcpSocket.writeData(socket, channelList);
            } else if (defaultData instanceof ChannelJoinPart) {
                ChannelJoinPart channelJoinPart = ((ChannelJoinPart) defaultData);
                channelJoinPart.setUser(user);
                if (channelJoinPart.hasJoined()) {
                    if (!channelDatabase.channelExists(channelJoinPart.getChannel())) {
                        notifyChannelStatusChange(channelJoinPart.getChannel(), true);
                    }
                    channelDatabase.addUserToChannel(channelJoinPart.getChannel(), channelJoinPart.getUser());
                } else {
                    channelDatabase.removeUserFromChannel(channelJoinPart.getChannel(), channelJoinPart.getUser());
                    if (!channelDatabase.channelExists(channelJoinPart.getChannel())) {
                        notifyChannelStatusChange(channelJoinPart.getChannel(), false);
                    }
                }
                ArrayList<Socket> userSocketList = (ArrayList<Socket>) channelDatabase.getSocketsOfUsersInChannel(channelJoinPart.getChannel(), channelJoinPart.getUser());
                sendToAllSockets(userSocketList, channelJoinPart);
                logger.info(channelJoinPart.toString());
            } else {
                logger.log(Level.WARNING, "Unhandled message: {0}", defaultData);
            }
        }
