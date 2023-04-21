    public String translate(TeamMessage m, Locale locale) {
        Client client = (Client) m.getSource();
        IRCMessage message = new IRCMessage(IRCCommand.PRIVMSG);
        message.setNick("jetrix");
        message.addParameter("#" + m.getChannelName());
        String messageKey = m.getName() == null ? "channel.team.none" : "channel.team.new";
        Object[] params = new Object[] { client.getUser().getName(), m.getName() };
        message.addParameter(applyStyle(Language.getText(messageKey, locale, params)));
        return message.toString();
    }
