    public void run(final IRCEvent e) {
        final MessageEvent chanEvent = (MessageEvent) e;
        ServerPanel panel = SandIRCFrame.getInstance().getServersPanel();
        ServerTreeNode node = panel.getOrCreateServerNode(e.getSession());
        final IRCWindowContainer sessionContainer = node.getContainer();
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                if (chanEvent.getType().equals(IRCEvent.Type.PRIVATE_MESSAGE)) {
                    IRCWindow window = sessionContainer.getPrivateMessageWindow(chanEvent.getNick(), e.getSession());
                    window.insertMsg(chanEvent.getNick(), chanEvent.getMessage());
                } else {
                    IRCWindow window = sessionContainer.findWindowByChannel(chanEvent.getChannel());
                    window.insertMsg(chanEvent.getNick(), chanEvent.getMessage());
                }
            }
        });
    }
