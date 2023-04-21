    private void assertChannels(final Integer... levelControlIndexes) {
        for (int channelIndex = 0; channelIndex < levelControlIndexes.length; channelIndex++) {
            LevelControl expectedLevelControl = null;
            if (levelControlIndexes[channelIndex] >= 0) {
                expectedLevelControl = getLevelControl(levelControlIndexes[channelIndex]);
            }
            LevelControl levelControl = detail.getChannelLevel(channelIndex).getLevelControl();
            assertEquals(levelControl, expectedLevelControl, "channel " + (channelIndex + 1));
        }
    }
