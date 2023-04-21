    public static ChatMessage create(InCommand command, String channelName, boolean isPrivate) {
        if (command instanceof CtcpMessage) {
            MessageCommand action = (MessageCommand) command;
            return new ChatMessage(MessageType.ACTION, action.getSource().getSource(), action.getMessage());
        } else if (command instanceof MessageCommand) {
            MessageCommand msg = (MessageCommand) command;
            return new ChatMessage(isPrivate ? MessageType.PRIVMSG : MessageType.MESSAGE, msg.getSource().getSource(), msg.getMessage());
        } else if (command instanceof JoinCommand) {
            JoinCommand join = (JoinCommand) command;
            return new ChatMessage(MessageType.INFO, join.getUser().getSource(), "has joined " + join.getChannel());
        } else if (command instanceof QuitCommand) {
            QuitCommand quit = (QuitCommand) command;
            return new ChatMessage(MessageType.INFO, quit.getUser().getSource(), "has left " + channelName + " : " + quit.getReason());
        } else if (command instanceof KickCommand) {
            KickCommand kick = (KickCommand) command;
            return new ChatMessage(MessageType.INFO, kick.getKicker().getSource(), "has kicked " + kick.getKicked().getSource() + " from " + kick.getChannel() + " : " + kick.getComment());
        } else if (command instanceof NickCommand) {
            NickCommand nick = (NickCommand) command;
            return new ChatMessage(MessageType.INFO, nick.getOldNick(), "is now known as " + nick.getNick());
        } else if (command instanceof ChannelModeCommand) {
            ChannelModeCommand mode = (ChannelModeCommand) command;
            String source = mode.getPrefix();
            if (source == null) {
                source = "null";
            }
            return new ChatMessage(MessageType.INFO, source, " sets " + mode.render());
        } else if (command instanceof TopicCommand) {
            TopicCommand topic = (TopicCommand) command;
            String source = Util.split(topic.getSourceString().substring(1), " ").get(0);
            return new ChatMessage(MessageType.INFO, source, "changes the topic to: " + topic.getTopic());
        }
        return null;
    }
