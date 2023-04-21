    private void assertChannelLevels(final LightCueDetail detail1, final LightCueDetail detail2) {
        int channelCount1 = detail1.getNumberOfChannels();
        int channelCount2 = detail2.getNumberOfChannels();
        int channelCount = Math.min(channelCount1, channelCount2);
        for (int i = 0; i < channelCount; i++) {
            CueChannelLevel level1 = detail1.getChannelLevel(i);
            CueChannelLevel level2 = detail2.getChannelLevel(i);
            String message = "cueChannelLevel[" + i + "]";
            assertEquals(level1, level2, message);
        }
        for (int i = channelCount; i < channelCount2; i++) {
            CueChannelLevel level = detail2.getChannelLevel(i);
            String message = "cueChannelLevel[" + i + "]";
            assertTrue(level.isDerived(), message);
            assertDefaultCueChannelLevel(message, level);
        }
    }
