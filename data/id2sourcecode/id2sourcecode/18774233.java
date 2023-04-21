    public void setChannelID(int chID) {
        channelID = chID;
        channel = ChannelFrame.channelGridPanel.channels[channelID];
        setOpaqueCheck(channel.getChannelDisplay().isOpaque());
        transparencySlider.setValue((int) (channel.getChannelDisplay().getAlpha() * 100));
    }
