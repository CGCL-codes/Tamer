    public String translate(LeaveMessage m, Locale locale) {
        IRCMessage message = new IRCMessage(IRCCommand.PART);
        message.setNick(m.getName());
        message.addParameter("#" + m.getChannelName());
        return message.toString();
    }
