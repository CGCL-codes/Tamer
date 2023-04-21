    private void assertCueChannelLevel(final LightCueDetail cue, final int channelIndex, final int intValue, final boolean active) {
        LevelValue levelValue = cue.getChannelLevel(channelIndex).getChannelLevelValue();
        assertEquals(intValue, levelValue.getIntValue());
        assertEquals(active, levelValue.isActive());
    }
