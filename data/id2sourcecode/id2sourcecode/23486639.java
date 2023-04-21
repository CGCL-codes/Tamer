    private void updateCueChannelSubmasterLevel(final int cueIndex, final int channelIndex, final float value, final boolean active) {
        LightCueDetail detail = cues.get(cueIndex);
        CueChannelLevel cueChannelLevel = detail.getChannelLevel(channelIndex);
        LevelValue levelValue = cueChannelLevel.getSubmasterLevelValue();
        levelValue.setValue(value);
        levelValue.setActive(active);
    }
