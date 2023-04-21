    public void testGetChannelName() {
        assertTrue(scheduleByChannelTest.getChannelName().equals("Rai2"));
        assertFalse(scheduleByChannelTest.getChannelName().equals("Rai3"));
    }
