    public String translate(EndGameMessage m, Locale locale) {
        IRCMessage message = new IRCMessage(IRCCommand.PRIVMSG);
        message.setNick("jetrix");
        message.addParameter("#" + m.getChannelName());
        message.addParameter(applyStyle(Language.getText("channel.game.stop", locale)));
        return message.toString();
    }
