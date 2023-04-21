    public Message getMessage(String line) {
        IRCMessage msg = IRCMessage.parse(line);
        if (msg.isCommand(IRCCommand.JOIN)) {
            AddPlayerMessage message = new AddPlayerMessage();
            message.setDestination(ChannelManager.getInstance().getChannel(msg.getParameter(0)));
            return message;
        } else if (msg.isCommand(IRCCommand.PART)) {
            LeaveMessage message = new LeaveMessage();
            message.setDestination(ChannelManager.getInstance().getChannel(msg.getParameter(0)));
            return message;
        } else if (msg.isCommand(IRCCommand.PRIVMSG)) {
            SmsgMessage message = new SmsgMessage();
            message.setDestination(ChannelManager.getInstance().getChannel(msg.getParameter(0)));
            message.setText(msg.getParameter(1));
            message.setPrivate(false);
            return message;
        } else {
            return null;
        }
    }
