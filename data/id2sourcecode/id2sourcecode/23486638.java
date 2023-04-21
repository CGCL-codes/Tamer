    private void updateCueChannelLevel(final int cueIndex, final int channelIndex, final int value, final boolean active) {
        LightCueDetail detail = cues.get(cueIndex);
        CueChannelLevel cueChannelLevel = detail.getChannelLevel(channelIndex);
        LevelValue levelValue = cueChannelLevel.getChannelLevelValue();
        levelValue.setIntValue(value);
        levelValue.setActive(active);
    }
