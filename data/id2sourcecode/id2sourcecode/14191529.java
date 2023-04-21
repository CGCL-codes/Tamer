    public void refreshIrcChatPane() {
        Vector list = ircMgr.getChannels();
        if (list.size() <= ircCurrentChannel) return;
        IrcChannel channel;
        try {
            channel = (IrcChannel) list.elementAt(ircCurrentChannel);
        } catch (Exception e) {
            return;
        }
        String logText = channel.getChatLogText(false);
        try {
            ircChatLogText.setDocument(new DefaultStyledDocument());
            ircChatLogDoc = (StyledDocument) ircChatLogText.getDocument();
            ircChatLogDoc.insertString(0, logText, ircChatLogStyle);
        } catch (BadLocationException ex) {
            ;
        }
    }
