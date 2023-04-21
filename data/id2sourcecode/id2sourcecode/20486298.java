    @Test
    public void constructor() {
        CueChannelLevel level = new CueChannelLevel();
        assertEquals(0, level.getChannelIntValue());
        assertEquals(0, level.getSubmasterIntValue());
        assertTrue(level.isDerived());
        assertTrue(level.isActive());
    }
