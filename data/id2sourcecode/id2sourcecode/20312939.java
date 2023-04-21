    public static IRCWindow getWindowForChannel(Channel channel, Session session, List<IRCWindow> windows) {
        List<IRCWindow> sessionWins = getWindowsForSession(session, windows);
        for (IRCWindow window : sessionWins) {
            if (channel.equals(window.getDocument().getChannel())) {
                return window;
            }
        }
        return null;
    }
