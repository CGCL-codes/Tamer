    public static int getChannels(IRCMessage msg) {
        try {
            return Integer.parseInt((String) msg.getArgs().elementAt(msg.getArgs().size() - 2));
        } catch (NumberFormatException f) {
            return 0;
        }
    }
