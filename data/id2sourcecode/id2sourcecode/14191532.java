    IrcChannel getSelectedChannel() {
        int row = ircChannelTable.getSelectedRow();
        if (row < 0 || row >= ircChannelTable.getRowCount()) return null;
        row = ircChannelSorter.indexes[row];
        Vector list = ircMgr.getChannels();
        return (IrcChannel) list.elementAt(row);
    }
