    public String translate(PlineActMessage m, Locale locale) {
        IRCMessage message = new IRCMessage(IRCCommand.PRIVMSG);
        Destination source = m.getSource();
        if (source != null && source instanceof Client) {
            message.setNick(((Client) source).getUser().getName());
        } else {
            message.setNick("jetrix");
        }
        String name = m.getChannelName() == null ? "#jetrix" : "#" + m.getChannelName();
        message.addParameter(name);
        message.addParameter(applyStyle("ACTION " + m.getText(locale) + ""));
        return message.toString();
    }
