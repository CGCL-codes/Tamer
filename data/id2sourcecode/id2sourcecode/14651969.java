    private void process(Message m) {
        if (m != null && m instanceof CommandMessage) {
            CommandMessage command = (CommandMessage) m;
            PlineMessage response = new PlineMessage();
            if ("listuser".equals(command.getCommand())) {
                StringBuilder message = new StringBuilder();
                for (Client client : ClientRepository.getInstance().getClients()) {
                    User user = client.getUser();
                    message.append("\"");
                    message.append(user.getName());
                    message.append("\" \"");
                    message.append(user.getTeam() == null ? "" : user.getTeam());
                    message.append("\" \"");
                    message.append(client.getAgent() + " " + client.getVersion());
                    message.append("\" ");
                    message.append(client.getChannel().getClientSlot(client));
                    message.append(" ");
                    message.append(user.isPlaying() ? "1" : "0");
                    message.append(" ");
                    message.append(user.getAccessLevel());
                    message.append(" \"");
                    message.append(client.getChannel().getConfig().getName());
                    message.append("\"");
                    message.append(QueryProtocol.EOL);
                }
                response.setText(message.toString());
            } else if ("listchan".equals(command.getCommand())) {
                StringBuilder message = new StringBuilder();
                for (Channel channel : ChannelManager.getInstance().channels()) {
                    ChannelConfig config = channel.getConfig();
                    if (config.isVisible()) {
                        message.append("\"");
                        message.append(config.getName());
                        message.append("\" \"");
                        message.append(config.getDescription());
                        message.append("\" ");
                        message.append(channel.getPlayerCount());
                        message.append(" ");
                        message.append(config.getMaxPlayers());
                        message.append(" 0 ");
                        message.append(channel.getGameState().getValue());
                        message.append(QueryProtocol.EOL);
                    }
                }
                response.setText(message.toString());
            } else if ("playerquery".equals(command.getCommand())) {
                response.setText("Number of players logged in: " + ClientRepository.getInstance().getClientCount());
            } else if ("version".equals(command.getCommand())) {
                response.setText("Jetrix/" + ServerConfig.VERSION + QueryProtocol.EOL);
            }
            send(response);
        } else {
            NoConnectingMessage noconnecting = new NoConnectingMessage();
            noconnecting.setText("Wrong command");
            send(noconnecting);
            disconnected = true;
        }
    }
