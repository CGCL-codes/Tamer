    public String translate(JoinMessage m, Locale locale) {
        IRCMessage message = new IRCMessage(IRCCommand.JOIN);
        message.setNick(m.getName());
        message.addParameter("#" + m.getChannelName());
        return message.toString();
    }
