    private void quit(IRCEvent e) {
        QuitEvent qe = (QuitEvent) e;
        List<Channel> chans = qe.getChannelList();
        for (Channel chan : chans) {
            Window win = WindowUtilites.getWindowForChannel(chan, e.getSession(), BaseWindow.getWindowList());
            win.insertDefault(qe.getWho() + " has quit - " + qe.getQuitMessage());
        }
    }
