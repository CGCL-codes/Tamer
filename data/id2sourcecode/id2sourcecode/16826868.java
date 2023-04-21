    private void scrollToChannel(final String channelID) {
        getProgrammesScrollPane().getVerticalScrollBar().setValue(getChannelNamePanel().getScrollValue(channelID));
    }
