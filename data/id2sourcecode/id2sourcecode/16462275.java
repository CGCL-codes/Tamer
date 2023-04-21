    private void assertChannel(final int expectedLevelControlIndex, final int channelIndex) {
        LevelControl expectedLevelControl = null;
        if (expectedLevelControlIndex >= 0) {
            expectedLevelControl = levelControls.get(expectedLevelControlIndex);
        }
        LevelControl levelControl = detail.getChannelLevel(channelIndex).getLevelControl();
        assertEquals(expectedLevelControl, levelControl);
    }
