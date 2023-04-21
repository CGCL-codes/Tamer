    public void test_setMessage2() throws Exception {
        ShortMessage1 message = new ShortMessage1();
        try {
            message.setMessage(245, 34, 56);
            fail("InvalidMidiDataException expected");
        } catch (InvalidMidiDataException e) {
        }
        try {
            message.setMessage(256, 34, 56);
            fail("InvalidMidiDataException expected");
        } catch (InvalidMidiDataException e) {
        }
        message.setMessage(250, 34, 56);
        assertEquals(10, message.getChannel());
        assertEquals(240, message.getCommand());
        assertEquals(0, message.getData1());
        assertEquals(0, message.getData2());
        assertEquals(1, message.getLength());
        assertEquals(250, message.getStatus());
        assertEquals(1, message.getMessage().length);
    }
