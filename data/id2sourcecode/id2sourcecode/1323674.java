    public void run(IRCEvent e) {
        final InviteEvent inviteEvent = (InviteEvent) e;
        ServerPanel panel = SandIRCFrame.getInstance().getServersPanel();
        ServerTreeNode node = panel.getOrCreateServerNode(e.getSession());
        IRCWindowContainer sessionContainer = node.getContainer();
        final IRCWindow win = sessionContainer.getSelectedWindow();
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                win.insertDefault("* " + inviteEvent.getNick() + " has invited you to " + inviteEvent.getChannelName());
            }
        });
    }
