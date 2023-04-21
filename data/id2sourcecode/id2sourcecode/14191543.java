    void removeIrcChannel() {
        int row = ircChannelTable.getSelectedRow();
        if (row < 0 || row >= ircChannelTable.getRowCount()) {
            JOptionPane.showMessageDialog(this, "Select the channel that you would like to remove from the list below.", "Select Channel", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Vector list = ircMgr.getChannels();
        IrcChannel channel = (IrcChannel) list.elementAt(row);
        channel.part();
        ircMgr.removeIrcChannel(channel);
        ircMgr.serializeChannels();
        ServiceManager.getCfg().save();
    }
