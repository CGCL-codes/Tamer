    public void stateChanged(ChangeEvent e) {
        if (tab.getTabbedPane().getTabCount() == 0) {
            setTitle(App.APP_SHORT_NAME + " " + App.APP_VERSION);
        } else {
            DefaultIRCSession s = null;
            Component c = tab.getTabbedPane().getSelectedComponent();
            String stuff = "";
            if (c instanceof IRCChatPanel) {
                AbstractIRCSession session = ((IRCChatPanel) c).getChannel().getParentSession();
                s = (DefaultIRCSession) session;
            } else if (c instanceof IRCSessionPanel) s = ((IRCSessionPanel) c).getSession();
            if (s != null) stuff = s.getConnection().getRemoteAddress().toString() + " (" + s.getUser().getNick() + ") - " + App.APP_SHORT_NAME + " " + App.APP_VERSION; else stuff = App.APP_SHORT_NAME + " " + App.APP_VERSION;
            setTitle(stuff);
        }
        App.gui.tabStateChanged(this);
    }
