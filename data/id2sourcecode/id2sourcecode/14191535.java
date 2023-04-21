    void ircSendChat() {
        String text = ircSendText.getText().trim();
        if (text.length() == 0) return;
        int row = ircChannelTable.getSelectedRow();
        if (row < 0 || row >= ircChannelTable.getRowCount()) return;
        Vector list = ircMgr.getChannels();
        IrcChannel channel = (IrcChannel) list.elementAt(row);
        channel.send(text);
        ircSendText.setText("");
    }
