    public static int getChannels(IRCMessage msg) {
        try {
            return Integer.parseInt(msg.getArgs().get(msg.getArgs().size() - 2));
        } catch (NumberFormatException f) {
            return 0;
        }
    }
