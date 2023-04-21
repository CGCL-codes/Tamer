    private void joinComplete(IRCEvent e) {
        JoinCompleteEvent je = (JoinCompleteEvent) e;
        Window win = new Window(je.getSession(), je.getChannel(), "", IRCDocument.Type.CHANNEL);
        BaseWindow bw = BaseWindow.getInstance();
        BaseWindow.getWindowList().add(win);
        bw.pane.add(je.getChannel().getName(), win);
    }
