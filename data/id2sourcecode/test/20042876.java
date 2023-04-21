    public void joinNick(String nick, String mode) {
        _nicks.put(nick.toLowerCase(java.util.Locale.ENGLISH), new Nick(nick, mode, getIRCServer().getChannelModes(), getIRCServer().getNickModes()));
        if (_ircConfiguration.getASLMaster()) getIRCServer().execute("WHO " + nick);
        _listeners.sendEvent("nickJoin", nick, mode, this);
    }
