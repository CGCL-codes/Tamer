    private void updateChannel(final int lightCueIndex, final int channelIndex, final boolean derived, final float value) {
        LightCueDetail detail = getDetail(lightCueIndex);
        CueChannelLevel level = detail.getChannelLevel(channelIndex);
        level.setDerived(derived);
        LevelValue levelValue = level.getChannelLevelValue();
        levelValue.setActive(true);
        levelValue.setValue(value);
        fireChannelLevelChanged(lightCueIndex, channelIndex);
        updateSubsequentChannels(lightCueIndex, channelIndex, value);
    }
