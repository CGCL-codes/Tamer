    public static String getChannel(IRCMessage msg) {
        try {
            MessageFormat format = new MessageFormat("is unknown mode char to me for {0}");
            Object[] stuff = format.parse((String) msg.getArgs().elementAt(1));
            return (String) stuff[0];
        } catch (ParseException e) {
            return "";
        }
    }
