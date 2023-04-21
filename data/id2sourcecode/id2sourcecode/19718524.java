    @Override
    public String getColumnName(int col) {
        if (channelList != null) {
            if (col == 0) {
                return "";
            }
            return channelList.getChannelAt(col - 1).getChannelName();
        }
        return null;
    }
