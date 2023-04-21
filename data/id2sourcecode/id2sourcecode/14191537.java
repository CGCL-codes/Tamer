    void joinFurthurChannel() {
        try {
            Vector list = ircMgr.getChannels();
            for (int i = 0; i < list.size(); i++) {
                if (((IrcChannel) list.elementAt(i)).getName().equals("#furthur")) {
                    ((IrcChannel) list.elementAt(i)).join();
                    ircChannelModel.fireTableDataChanged();
                    ircChannelTable.clearSelection();
                    int visibleRow = 0;
                    for (int j = 0; j < ircChannelSorter.indexes.length; j++) {
                        if (ircChannelSorter.indexes[j] == 0) {
                            visibleRow = j;
                            break;
                        }
                    }
                    ircChannelTable.addRowSelectionInterval(visibleRow, visibleRow);
                }
            }
        } catch (Exception e) {
        }
    }
