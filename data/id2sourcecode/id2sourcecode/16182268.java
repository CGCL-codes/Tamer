    private void adaptTitle(String nick) {
        String title = App.APP_SHORT_NAME + " " + App.APP_VERSION;
        if (tab.getTabbedPane().getTabCount() == 0) {
            setTitle(title);
            scannerBar.stop();
            return;
        }
        AbstractIRCSession s = null;
        Component c = tab.getTabbedPane().getSelectedComponent();
        if (c instanceof IRCChatPanel) s = ((IRCChatPanel) c).getChannel().getParentSession(); else if (c instanceof IRCSessionPanel) s = ((IRCSessionPanel) c).getSession(); else {
            resetSession();
            setTitle(title);
            scannerBar.stop();
            return;
        }
        if (s != currentSession) {
            resetSession();
            currentSession = s;
            currentSession.getConnection().addIRCConnectionListener(this);
            if (currentSession.getConnection().getStatus() == SessionStatus.CONNECTING || currentSession.getConnection().getStatus() == SessionStatus.AUTHENTICATING) {
                scannerBar.start();
            } else scannerBar.stop();
        }
        if (currentSession != null) {
        }
        setTitle(title);
    }
