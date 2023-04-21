    public static String getChannel(IRCMessage msg) {
        try {
            return msg.getArgs().get(0);
        } catch (IndexOutOfBoundsException e) {
            return "";
        }
    }
