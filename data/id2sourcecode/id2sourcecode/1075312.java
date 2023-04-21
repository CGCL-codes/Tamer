    public static ChannelPanel getChannelPanel(int index) {
        if (index < channelPanels.size()) {
            return channelPanels.get(index);
        } else {
            return null;
        }
    }
