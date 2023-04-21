    public void run(IRCEvent e) {
        final JoinEvent je = (JoinEvent) e;
        ServerPanel panel = SandIRCFrame.getInstance().getServersPanel();
        ServerTreeNode node = panel.getOrCreateServerNode(e.getSession());
        IRCWindowContainer sessionContainer = node.getContainer();
        final IRCWindow window = sessionContainer.findWindowByChannel(je.getChannel());
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                window.updateUsersList();
                window.insertDefault("*** " + je.getNick() + " [" + je.getUserName() + "@" + je.getHostName() + "] has joined " + je.getChannelName());
            }
        });
    }
