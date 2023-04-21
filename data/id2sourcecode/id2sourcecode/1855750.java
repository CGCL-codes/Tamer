    private void patternToChannel(PatternInterface pattern, int channelIndex) {
        ChannelFrame.channelGridPanel.channels[channelIndex].setPatternType(pattern);
        ChannelFrame.controlPanel.clipParametersPanel.loadParameters();
        ChannelFrame.channelGridPanel.channels[channelIndex].getChannelBeat().setGearIndex(pattern.getInitDelayIndex(), false);
        ChannelFrame.controlPanel.delayPanel.loadValue();
    }
