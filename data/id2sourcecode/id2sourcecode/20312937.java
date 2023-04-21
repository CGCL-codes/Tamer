    public static List<IRCWindow> getWindowsForNick(String nick, Session session, List<IRCWindow> windows) {
        List<IRCWindow> returnList = new ArrayList<IRCWindow>();
        List<IRCWindow> sessionWins = getWindowsForSession(session, windows);
        if (nick.equals(session.getNick())) {
            return sessionWins;
        }
        for (IRCWindow win : sessionWins) {
            IRCDocument doc = win.getDocument();
            if ((doc.getType() == IRCDocument.Type.PRIV) && (nick.equals(doc.getNick()) || session.getNick().equals(nick))) {
                returnList.add(win);
            } else if (doc.getType() != IRCDocument.Type.PRIV) {
                Channel chan = doc.getChannel();
                if (chan.getNicks().contains(nick)) {
                    returnList.add(win);
                }
            }
        }
        return returnList;
    }
