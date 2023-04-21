    private void irchandler_QUIT(String text, MsgIRC inMsg) {
        String user = inMsg.getSender();
        String channel = inMsg.getParam1();
        IrcMsg msg = new IrcMsg(null, user + " has quit IRC (" + channel + ")");
        Vector channels = getChannelsContainingUser(user);
        for (int i = 0; i < channels.size(); i++) {
            IrcChannel ch = (IrcChannel) channels.elementAt(i);
            ch.addMsg(msg);
            ch.deleteUser(user);
        }
    }
