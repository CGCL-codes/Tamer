    public static Window getWindowForChannel(Channel channel, Session session, List<Window> windows) {
        List<Window> sessionWins = getWindowsForSession(session, windows);
        for (Window window : sessionWins) {
            if (channel.equals(window.getDocument().getChannel())) {
                return window;
            }
        }
        return null;
    }
