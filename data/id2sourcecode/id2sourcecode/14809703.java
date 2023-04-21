    public java.lang.Object getValueAt(int row, int column) {
        int k = 0;
        if (column == 0) return new Integer(row + 1);
        for (int i = 0; i < storage.getGroupsSize(); i++) {
            for (int j = 0; j < storage.getChannelsSize(i); j++) {
                k++;
                if (k == column) {
                    DataChannel channel = storage.getChannel(i, j);
                    if (row < channel.size()) return new Double(channel.getData(row));
                }
            }
        }
        return "--";
    }
