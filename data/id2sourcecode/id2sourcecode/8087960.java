    public void choiceMade(PatternInterface clip0, PatternInterface clip1, PatternInterface clip2) {
        ChannelFrame.channelGridPanel.channels[channelID].setPatternType(clip0);
        ChannelFrame.controlPanel.clipParametersPanel.loadParameters();
        ChannelFrame.channelGridPanel.channels[channelID].getChannelBeat().setGear(0.125, false);
        ChannelFrame.controlPanel.delayPanel.loadValue();
    }
