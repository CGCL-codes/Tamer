        @Override
        public void incomingMessage(NetEvent event) {
            ResourceBundle messages = ResourceBundle.getBundle("portochat/resource/MessagesBundle", java.util.Locale.getDefault());
            DefaultData defaultData = event.getData();
            if (logger.isLoggable(Level.FINE)) {
                logger.fine(defaultData.toString());
            }
            if (defaultData instanceof Pong) {
                System.out.println(messages.getString("ServerConnection.msg.ServerLag") + ((Pong) defaultData).getCalculatedLag() + messages.getString("ServerConnection.msg.Ms"));
            } else if (defaultData instanceof ServerMessage) {
                ServerMessage message = (ServerMessage) defaultData;
                if (message.getMessageEnum().equals(ServerMessageEnum.USERNAME_SET)) {
                    for (ServerDataListener listener : listeners) {
                        listener.handleServerConnection(message.getAdditionalMessage(), true);
                    }
                } else if (message.getMessageEnum().equals(ServerMessageEnum.ERROR_USERNAME_IN_USE)) {
                    for (ServerDataListener listener : listeners) {
                        listener.handleServerConnection(message.getAdditionalMessage(), false);
                    }
                }
            } else if (defaultData instanceof ChatMessage) {
                ChatMessage message = (ChatMessage) defaultData;
                String channel = message.isChannel() ? message.getTo() : null;
                for (ServerDataListener listener : listeners) {
                    listener.receiveChatMessage(message.getFromUser(), message.isAction(), message.getMessage(), channel);
                }
            } else if (defaultData instanceof UserList) {
                UserList userList = (UserList) defaultData;
                String channel = userList.getChannel();
                for (ServerDataListener listener : listeners) {
                    listener.userListReceived(userList.getUserList(), channel);
                }
            } else if (defaultData instanceof UserConnection) {
                UserConnection user = (UserConnection) defaultData;
                for (ServerDataListener listener : listeners) {
                    listener.userConnectionEvent(user.getUser(), user.isConnected());
                }
            } else if (defaultData instanceof ChannelList) {
                ChannelList channelList = (ChannelList) defaultData;
                List<String> channels = channelList.getChannelList();
                for (ServerDataListener listener : listeners) {
                    listener.channelListReceived(channels);
                }
            } else if (defaultData instanceof ChannelJoinPart) {
                ChannelJoinPart joinPart = (ChannelJoinPart) defaultData;
                for (ServerDataListener listener : listeners) {
                    listener.receiveChannelJoinPart(joinPart.getUser(), joinPart.getChannel(), joinPart.hasJoined());
                }
            } else if (defaultData instanceof ChannelStatus) {
                ChannelStatus status = (ChannelStatus) defaultData;
                for (ServerDataListener listener : listeners) {
                    listener.channelStatusReceived(status.getChannel(), status.isCreated());
                }
            } else if (defaultData instanceof Initialization) {
                Initialization init = (Initialization) defaultData;
                if (init.getInitializationEnum() == InitializationEnum.READY) {
                    sendUsername(username);
                }
            } else {
                logger.warning(messages.getString("ServerConnection.msg.UnknownMessage") + defaultData);
            }
        }
