    @Test
    public void channelLevelChanged() {
        addCue();
        addCue();
        TestingCuesListener listener = new TestingCuesListener();
        lightCues.addListener(listener);
        assertEquals(listener.getNumberOfChannelChanges(), 0);
        lightCues.setChannel(0, 0, 0.5f);
        assertEquals(listener.getNumberOfChannelChanges(), 2);
        assertEquals(listener.getChannelChangeCueIndex(0), 0);
        assertEquals(listener.getChannelChangeIndex(0), 0);
        assertEquals(listener.getChannelChangeCueIndex(1), 1);
        assertEquals(listener.getChannelChangeIndex(1), 0);
    }
