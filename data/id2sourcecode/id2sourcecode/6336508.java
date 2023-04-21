    public String getChannelName(int channelIndex) {
        try {
            return getChannelList().get(channelIndex).channelID;
        } catch (Exception e) {
            return null;
        }
    }
