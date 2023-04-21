    private void channelMsg(IRCEvent e) {
        MessageEvent chanEvent = (MessageEvent) e;
        Window window = WindowUtilites.getWindowForChannel(chanEvent.getChannel(), chanEvent.getSession(), BaseWindow.getWindowList());
        window.insertMsg(chanEvent.getNick(), chanEvent.getMessage());
    }
