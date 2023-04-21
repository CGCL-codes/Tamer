    public static String getChannel(IRCMessage msg) {
        try {
            return (String) msg.getArgs().elementAt(0);
        } catch (ArrayIndexOutOfBoundsException e) {
            return "";
        }
    }
