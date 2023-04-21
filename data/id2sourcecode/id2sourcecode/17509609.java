    @Override
    public void process(IMessage message) {
        ChatClient ui = ChatClient.getInstance();
        MsgChat msg = (MsgChat) message;
        LOGGER.info("Processing : " + msg.getCommand() + msg.getMsg());
        if (msg.getCommand().equals(ChatCommands.PONG)) {
        } else if (msg.getCommand().equals(ChatCommands.PM)) {
            ui.joinChannel(ChatCommands.PM_PREFIX + msg.getSender() + msg.getMsg());
        } else if (msg.getCommand().equals(ChatCommands.MEMBERS)) {
            ui.setVisible(true);
            ui.setTitle("Chat Client " + GameContext.getUserName());
            ui.createChannelFrame(msg.getSender());
            ui.addUserToChannel(msg.getMsg(), msg.getSender());
        } else if (msg.getCommand().equals(ChatCommands.LOGIN_FAILED)) {
            ui.showMessage("Login failed");
        } else if (msg.getCommand().equals(ChatCommands.NEW_CHANNEL)) {
            ui.addChannel(msg.getMsg());
        } else if (msg.getCommand().equals(ChatCommands.EMPTY_CHANNEL)) {
            ui.removeChannel(msg.getMsg());
        } else if (msg.getCommand().equals(ChatCommands.CHANNEL_LIST)) {
            ui.addChannel(msg.getMsg());
        } else if (msg.getCommand().equals(ChatCommands.JOINED)) {
            ClientChannel ch;
            Set<ClientChannel> channels = GameContext.getClientCommunication().getChannelConteiner().getChannelsOfType(ChannelNameParser.parseChannelType(msg.getSender()));
            if ((channels != null) && (!channels.isEmpty())) ch = (ClientChannel) channels.toArray()[0]; else ch = null;
            if (ch != null) ui.addUserToChannel(msg.getMsg(), ch.getName());
        } else if (msg.getCommand().equals(ChatCommands.LEFT)) {
            ui.removeUserFromChannel(msg.getMsg(), msg.getSender());
        } else if (msg.getCommand().equals(ChatCommands.CHANNEL_MSG)) {
            String[] args = msg.getMsg().split(" ", 2);
            ui.addChannelMessage(args[0], msg.getSender(), args[1]);
        } else {
        }
    }
