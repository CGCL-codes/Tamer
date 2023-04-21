    public void run(IRCEvent e) {
        final PartEvent pe = (PartEvent) e;
        if (pe.getWho().equals(e.getSession().getNick())) return;
        ServerPanel panel = SandIRCFrame.getInstance().getServersPanel();
        ServerTreeNode node = panel.getOrCreateServerNode(e.getSession());
        IRCWindowContainer sessionContainer = node.getContainer();
        final IRCWindow win = sessionContainer.findWindowByChannel(pe.getChannel());
        if (win == null) return;
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                win.updateUsersList();
                win.insertDefault("*** " + pe.getWho() + " [" + pe.getUserName() + "@" + pe.getHostName() + "] has left " + pe.getChannelName() + " [" + pe.getPartMessage() + "]");
            }
        });
    }
