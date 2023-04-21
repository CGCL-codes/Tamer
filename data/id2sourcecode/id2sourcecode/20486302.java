    private void assertString(final String expected, final float channelValue, final float submasterValue, final boolean channelActive, final boolean submasterActive) {
        CueChannelLevel level = new CueChannelLevel();
        LevelValue clv = level.getChannelLevelValue();
        clv.setValue(channelValue);
        clv.setActive(channelActive);
        LevelValue slv = level.getSubmasterLevelValue();
        slv.setValue(submasterValue);
        slv.setActive(submasterActive);
        assertEquals(expected, level.string());
    }
