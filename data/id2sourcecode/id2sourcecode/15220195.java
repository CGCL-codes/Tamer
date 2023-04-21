    public static List<IRCWindow> getWindowsForNick(String nick, Session session, List<IRCWindow> windows) {
        List<IRCWindow> returnList = new ArrayList<IRCWindow>();
        if (nick.equals(session.getNick())) {
            return windows;
        }
        for (IRCWindow win : windows) {
            IRCDocument doc = win.getDocument();
            if (nick.equals(doc.getNick())) {
                returnList.add(win);
            } else if (doc.getType() != IRCWindow.Type.PRIVATE) {
                Channel chan = doc.getChannel();
                if (chan.getNicks().contains(nick)) {
                    returnList.add(win);
                }
            }
        }
        return returnList;
    }
