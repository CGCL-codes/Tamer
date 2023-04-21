    public void run(IRCEvent e) {
        final JoinCompleteEvent je = (JoinCompleteEvent) e;
        ServerPanel panel = SandIRCFrame.getInstance().getServersPanel();
        ServerTreeNode node = panel.getOrCreateServerNode(je.getSession());
        final IRCWindowContainer sessionContainer = node.getContainer();
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                sessionContainer.findWindowByChannel(je.getChannel());
            }
        });
    }
