    public int getColumnCount() {
        if (channelList != null && channelList.getChannelCount() > 0) {
            return channelList.getChannelCount() + 1;
        }
        return 0;
    }
