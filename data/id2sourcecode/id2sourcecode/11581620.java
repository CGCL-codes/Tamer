    public static String getChannel(IRCMessage msg) {
        return (String) msg.getArgs().elementAt(msg.getArgs().size() - 3);
    }
