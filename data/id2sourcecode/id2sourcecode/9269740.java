    private void part(IRCEvent e) {
        PartEvent pe = (PartEvent) e;
        Window win = WindowUtilites.getWindowForChannel(pe.getChannel(), e.getSession(), BaseWindow.getWindowList());
        win.insertDefault(pe.getWho() + " has left " + pe.getChannelName() + " - " + pe.getPartMessage());
    }
