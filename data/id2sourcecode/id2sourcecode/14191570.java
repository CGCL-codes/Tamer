        void fireChanged(Object source) {
            int row = ircChannelTable.getSelectedRow();
            if (row < 0 || row >= ircChannelTable.getRowCount()) return;
            row = ircChannelSorter.indexes[row];
            Vector list = ircMgr.getChannels();
            if (row >= list.size()) {
                return;
            }
            IrcChannel channel = (IrcChannel) list.elementAt(row);
            fireContentsChanged(source, 0, channel.getUserCount());
        }
