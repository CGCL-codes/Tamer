    public void execute(CommandMessage m) {
        Client client = (Client) m.getSource();
        ChannelManager channelManager = ChannelManager.getInstance();
        Locale locale = client.getUser().getLocale();
        String playerChannel = "";
        if (client.getChannel() != null) playerChannel = client.getChannel().getConfig().getName();
        PlineMessage response = new PlineMessage();
        response.setKey("command.list.header");
        client.send(response);
        int i = 0;
        for (Channel channel : channelManager.channels()) {
            i++;
            ChannelConfig conf = channel.getConfig();
            if (!conf.isVisible()) {
                continue;
            }
            if (client.getUser().isPlayer() && !conf.isProtocolAccepted(client.getProtocol().getName())) {
                continue;
            }
            StringBuilder message = new StringBuilder();
            message.append("<darkBlue>(" + (playerChannel.equals(conf.getName()) ? "<red>" + i + "</red>" : "<purple>" + i + "</purple>") + ") ");
            message.append("<purple>" + rightPad(conf.getName(), 6) + "</purple>\t");
            if (channel.isFull()) {
                message.append("[<red>" + Language.getText("command.list.status.full", locale) + "</red>]");
                for (int j = 0; j < 11 - Language.getText("command.list.status.full", locale).length(); j++) {
                    message.append(" ");
                }
            } else {
                message.append("[<aqua>" + Language.getText("command.list.status.open", locale) + "</aqua><blue>-" + channel.getPlayerCount() + "/" + conf.getMaxPlayers() + "</blue>]");
            }
            if (channel.getGameState() != STOPPED) {
                message.append(" <gray>{" + Language.getText("command.list.status.ingame", locale) + "}</gray> ");
            } else {
                message.append("                  ");
            }
            message.append("<black>" + conf.getDescription());
            client.send(new PlineMessage(message.toString()));
        }
    }
