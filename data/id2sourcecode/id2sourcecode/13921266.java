    private String getFullModeNick(String nick, String mode) {
        Channel c = (Channel) getSource();
        char[] _prefixes = c.getIRCServer().getNickPrefixes();
        char[] _modes = c.getIRCServer().getNickModes();
        char[][] chanmodes = c.getIRCServer().getChannelModes();
        ModeHandler h = new ModeHandler(mode, chanmodes, _modes);
        for (int i = 0; i < _modes.length; i++) {
            if (h.hasMode(_modes[i])) return _prefixes[i] + nick;
        }
        return nick;
    }
