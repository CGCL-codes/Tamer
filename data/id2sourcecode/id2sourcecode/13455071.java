    private boolean isChannel(String name, Source source) {
        if (name.length() == 0) return false;
        Server s = source.getServer();
        if (s instanceof IRCServer) {
            char[] prefixes = ((IRCServer) s).getChannelPrefixes();
            for (int i = 0; i < prefixes.length; i++) if (name.charAt(0) == prefixes[i]) return true;
        }
        return false;
    }
