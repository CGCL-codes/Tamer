    private void assertCueChannelSubmasterActive(final int cueIndex, final int channelIndex, final boolean expected) {
        LevelValue value = getDetail(cueIndex).getChannelLevel(channelIndex).getSubmasterLevelValue();
        assertEquals(value.isActive(), expected);
    }
