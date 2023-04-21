    public static String getChannel(IRCMessage msg) {
        try {
            return (String) msg.getArgs().elementAt(2);
        } catch (ArrayIndexOutOfBoundsException e) {
            return "";
        }
    }
