    public void setChannelFocus(int channelID) {
        setChannelFocusBorder(currentChannelFocus);
        if (channelID >= channels.length) return;
        channels[channelID].getChannelDisplay().requestFocusInWindow();
        currentChannelFocus = channelID;
        deactivateAllBorders();
        setChannelFocusBorder(channelID);
        setOutputChannelBorder(currentOutputChannel);
        ChannelFrame.slideFocusedChannelUnderTop();
        ChannelFrame.controlPanel.setChannelID(channelID);
        ChannelFrame.filterPanel.channelTransitions.setChannelsForFadeTransition(this.getFocusChannel(), this.getOutPutChannel());
    }
