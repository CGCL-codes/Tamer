    public void run(final IRCEvent e) {
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                ServerPanel panel = SandIRCFrame.getInstance().getServersPanel();
                ServerTreeNode node = panel.getOrCreateServerNode(e.getSession());
                IRCWindowContainer sessionContainer = node.getContainer();
                IRCWindow noticeWindow = sessionContainer.getNoticeWindow();
                ChannelListEvent cle = (ChannelListEvent) e;
                noticeWindow.insertDefault(cle.getChannelName() + " " + cle.getNumberOfUser() + "[" + cle.getTopic() + "]");
            }
        });
    }
