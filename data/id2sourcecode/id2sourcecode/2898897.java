    public int getScrollValue(final String channelID) {
        for (int i = 0; i < displayedChannels.length; i++) {
            if (displayedChannels[i].getChannel().getID().equals(channelID)) {
                return (i - 2) * controller.config.sizeChannelHeight;
            }
        }
        return 0;
    }
