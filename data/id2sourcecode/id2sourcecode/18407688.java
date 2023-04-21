    public String translate(SpectatorListMessage m, Locale locale) {
        IRCMessage message1 = new IRCMessage(IRCReply.RPL_NAMREPLY);
        message1.setNick("jetrix");
        message1.addParameter(((Client) m.getDestination()).getUser().getName());
        message1.addParameter("=");
        message1.addParameter("#" + m.getChannel());
        Collection<String> spectators = m.getSpectators();
        message1.addParameter(StringUtils.join(spectators.iterator(), " "));
        IRCMessage message2 = new IRCMessage(IRCReply.RPL_ENDOFNAMES);
        message2.setNick("jetrix");
        message2.addParameter(((Client) m.getDestination()).getUser().getName());
        message2.addParameter("#" + m.getChannel());
        message2.addParameter("End of /NAMES list");
        return message1.toString() + getEOL() + message2;
    }
