    public void beatRefresh(int channelBeat) {
        channelDisplay.beatRefresh(channelBeat);
        outputPreviewPanel.beatRefresh(channelBeat);
        outputDisplayPanel.beatRefresh(channelBeat);
        if (channelID == ChannelFrame.controlPanel.getChannelID()) {
            ChannelFrame.controlPanel.indicatorPanel.beatRefresh(channelBeat);
        }
    }
