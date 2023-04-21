    public void run(IRCEvent e) {
        final ModeEvent me = (ModeEvent) e;
        Channel chan = me.getChannel();
        Pattern p = Pattern.compile("^:\\S+\\sMODE\\s\\S+\\s+(.+)$");
        final Matcher m = p.matcher(e.getRawEventData());
        m.matches();
        ServerPanel panel = SandIRCFrame.getInstance().getServersPanel();
        ServerTreeNode node = panel.getOrCreateServerNode(e.getSession());
        IRCWindowContainer sessionContainer = node.getContainer();
        final IRCWindow window = sessionContainer.findWindowByChannel(chan);
        if (window == null) return;
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                window.updateUsersList();
                window.insertDefault("** mode" + (me.getModeMap().size() > 1 ? "s" : "") + " [" + m.group(1) + "] set by " + me.setBy());
            }
        });
    }
