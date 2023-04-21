    public Channel getChannel(int row) {
        if (row >= 0 && row < getRowCount()) return channelList.get(row);
        return null;
    }
