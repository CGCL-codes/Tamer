    public void testChannelNumber() {
        int CHANNEL = 19;
        TestMidiChannel channel = new TestMidiChannel(CHANNEL);
        assertEquals("channel number", CHANNEL, channel.getChannelNumber());
    }
