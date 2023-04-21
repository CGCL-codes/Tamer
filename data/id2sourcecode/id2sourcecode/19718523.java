    public int getRowCount() {
        if (channelList != null && channelList.getChannelCount() > 0) {
            return channelList.getStartTimes().size();
        }
        return 0;
    }
