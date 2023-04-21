    private void processEnterChatMessage() {
        String text = ircSendText.getText().trim();
        if (text.length() == 0) return;
        int row = ircChannelTable.getSelectedRow();
        if (row < 0 || row >= ircChannelTable.getRowCount()) return;
        Vector list = ircMgr.getChannels();
        IrcChannel channel = (IrcChannel) list.elementAt(row);
        channel.send(text);
        ircSendText.setText("");
        int messageHistorySize = messageHistory.size();
        while (messageHistorySize > ServiceManager.getCfg().mMaxMessageHistorySize) {
            messageHistory.remove(0);
            messageHistorySize = messageHistory.size();
        }
        if (messageHistorySize != 0) {
            messageHistory.add((messageHistory.size() - 1), text);
            messageHistory.set((messageHistory.size() - 1), "");
        } else {
            messageHistory.add(0, text);
            messageHistory.add(1, "");
        }
        messagePointer = messageHistory.size() - 1;
    }
