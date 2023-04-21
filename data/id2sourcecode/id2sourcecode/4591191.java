    private void updateCueChannelSubmasterLevel(final LightCueDetail cueDetail, final int channelIndex, final float value, final boolean active) {
        CueChannelLevel level = cueDetail.getChannelLevel(channelIndex);
        LevelValue levelValue = level.getSubmasterLevelValue();
        levelValue.setValue(value);
        levelValue.setActive(active);
    }
