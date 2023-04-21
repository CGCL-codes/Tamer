    private void assertChannelActive(final int cueIndex, final int channelIndex, final boolean expected) {
        LevelValue value = getDetail(cueIndex).getChannelLevel(channelIndex).getChannelLevelValue();
        assertEquals(value.isActive(), expected);
    }
