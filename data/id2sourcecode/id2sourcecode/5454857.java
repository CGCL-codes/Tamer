    public void run(final IRCEvent e) {
        WhoisEvent we = (WhoisEvent) e;
        StringBuilder channels = new StringBuilder();
        if (we.getChannelNames() != null) {
            for (String chan : we.getChannelNames()) {
                channels.append(chan + " ");
            }
        }
        final StringBuilder buff = new StringBuilder();
        buff.append("** Whois \n");
        buff.append("** Nick:" + we.getNick() + "[" + we.getUser() + "@" + we.getHost() + "]\n");
        buff.append("** Real:" + we.getRealName() + "\n");
        buff.append("** Server:" + we.whoisServer() + "\n");
        buff.append("** Signon Time:" + we.signOnTime() + "\n");
        buff.append(we.isIdle() ? "** Idle Time:" + we.secondsIdle() + "\n" : "");
        buff.append(channels.length() > 0 ? "** Channels:" + channels : "");
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                ServerPanel panel = SandIRCFrame.getInstance().getServersPanel();
                ServerTreeNode node = panel.getOrCreateServerNode(e.getSession());
                IRCWindowContainer sessionContainer = node.getContainer();
                sessionContainer.getSelectedWindow().insertDefault(buff.toString());
            }
        });
    }
