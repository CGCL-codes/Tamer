    public Object getValueAt(int row, int col) {
        if (channelList != null) {
            if (col == 0) {
                if (row < channelList.getStartTimes().size()) {
                    return channelList.getStartTimes().get(row).toString();
                }
                return null;
            }
            Channel channel = channelList.getChannelAt(col - 1);
            int h = channelList.getStartTimes().get(row).first();
            int m = channelList.getStartTimes().get(row).second();
            String showName = channel.getShowStartingAt(h, m);
            return showName;
        }
        return null;
    }
