    public PluginResult handle(Bot bot, InCommand command) throws Exception {
        log.debug("LOG " + command.getClass() + ":" + Util.toString(command));
        if (command instanceof CtcpMessage) {
            CtcpMessage msg = (CtcpMessage) command;
            String action = msg.getAction();
            if (action.equals("ACTION")) {
                log(msg.getSource().getSource(), msg.getMessage(), LogType.ACTION);
            }
        } else if (command instanceof MessageCommand) {
            MessageCommand msg = (MessageCommand) command;
            if (!msg.isPrivateToUs(bot.getState())) {
                log(msg.getSource().getSource(), msg.getMessage(), LogType.PRIVMSG);
            }
        } else if (command instanceof JoinCommand) {
            JoinCommand join = (JoinCommand) command;
            log(join.getUser().getSource(), "has joined " + join.getChannel(), LogType.INFO);
        } else if (command instanceof QuitCommand) {
            QuitCommand quit = (QuitCommand) command;
            log(quit.getUser().getSource(), "has left " + bot.getChannelName() + " : " + quit.getReason(), LogType.INFO);
        } else if (command instanceof KickCommand) {
            KickCommand kick = (KickCommand) command;
            log(kick.getKicker().getSource(), "has kicked " + kick.getKicked().getSource() + " from " + kick.getChannel() + " : " + kick.getComment(), LogType.INFO);
        } else if (command instanceof NickCommand) {
            NickCommand nick = (NickCommand) command;
            log(nick.getOldNick(), "is now known as " + nick.getNick(), LogType.INFO);
        } else if (command instanceof ChannelModeCommand) {
            ChannelModeCommand mode = (ChannelModeCommand) command;
            String source = mode.getPrefix();
            if (source == null) {
                source = "null";
            }
            log(source, " sets " + mode.render(), LogType.INFO);
        } else if (command instanceof TopicCommand) {
            TopicCommand topic = (TopicCommand) command;
            String source = Util.split(topic.getSourceString().substring(1), " ").get(0);
            log(source, "changes the topic to: " + topic.getTopic(), LogType.INFO);
        }
        return PluginResult.NEXT;
    }
