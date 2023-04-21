    public static List<Window> getWindowsForNick(String nick, Session session, List<Window> windows) {
        List<Window> returnList = new ArrayList<Window>();
        List<Window> sessionWins = getWindowsForSession(session, windows);
        if (nick.equals(session.getNick())) {
            return sessionWins;
        }
        for (Window win : sessionWins) {
            IRCDocument doc = win.getDocument();
            if (doc.getType() == IRCDocument.Type.PRIV && (nick.equals(doc.getNick()) || session.getNick().equals(nick))) {
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
