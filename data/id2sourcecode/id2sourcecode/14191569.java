        public int getSize() {
            int row = ircChannelTable.getSelectedRow();
            if (row < 0 || row >= ircChannelTable.getRowCount()) return 0;
            row = ircChannelSorter.indexes[row];
            Vector list = ircMgr.getChannels();
            if (row >= list.size()) {
                return 0;
            }
            IrcChannel channel = (IrcChannel) list.elementAt(row);
            return channel.getUserCount();
        }
