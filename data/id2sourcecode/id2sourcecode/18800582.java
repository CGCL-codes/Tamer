    @Test
    public void channelChangeCurrentCue() {
        getCues().setCurrent(0);
        getCues().getLightCues().setChannel(0, 0, 0.5f);
        assertEquals(lastLayerId, Lanbox.ENGINE_SHEET);
        assertEquals(lastChange.getChannelId(), 1);
        assertEquals(lastChange.getDmxValue(), 127);
    }
