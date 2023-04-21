    private void updateState() {
        if (App.gui.getFocussedMainFrame() == null) {
            resetSession();
            setEnabled(false);
            return;
        }
        AbstractIRCSession s = null;
        Component c = App.gui.getFocussedMainFrame().getTabbedChannelContainer().getTabbedPane().getSelectedComponent();
        if (c instanceof IRCChatPanel) {
            s = ((IRCChatPanel) c).getChannel().getParentSession();
        } else if (c instanceof IRCSessionPanel) {
            s = ((IRCSessionPanel) c).getSession();
        } else {
            resetSession();
            setEnabled(false);
            return;
        }
        if (s == currentSession) {
            return;
        } else {
            resetSession();
            currentSession = s;
        }
        setEnabled(currentSession.isActive());
        currentSession.getConnection().addIRCConnectionListener(this);
    }
