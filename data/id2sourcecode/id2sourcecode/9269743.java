    private void join(IRCEvent e) {
        JoinEvent je = (JoinEvent) e;
        Window window = WindowUtilites.getWindowForChannel(je.getChannel(), je.getSession(), BaseWindow.getWindowList());
        window.insertDefault(je.getNick() + " has joined " + je.getChannelName());
    }
