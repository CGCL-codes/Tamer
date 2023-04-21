    public static String getChannel(IRCMessage msg) {
        try {
            return msg.getArgs().get(2);
        } catch (IndexOutOfBoundsException e) {
            return "";
        }
    }
