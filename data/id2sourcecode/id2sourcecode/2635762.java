    public void test_setMessage3() throws Exception {
        ShortMessage1 message = new ShortMessage1();
        try {
            message.setMessage(127, 10, 34, 56);
            fail("InvalidMidiDataException expected");
        } catch (InvalidMidiDataException e) {
        }
        try {
            message.setMessage(240, 34, 56, 13);
            fail("InvalidMidiDataException expected");
        } catch (InvalidMidiDataException e) {
        }
        try {
            message.setMessage(200, -1, 34, 56);
            fail("InvalidMidiDataException expected");
        } catch (InvalidMidiDataException e) {
        }
        try {
            message.setMessage(200, 16, 34, 56);
            fail("InvalidMidiDataException expected");
        } catch (InvalidMidiDataException e) {
        }
        try {
            message.setMessage(200, 12, -1, 56);
            fail("InvalidMidiDataException expected");
        } catch (InvalidMidiDataException e) {
        }
        try {
            message.setMessage(225, 8, 34, 456);
            fail("InvalidMidiDataException expected");
        } catch (InvalidMidiDataException e) {
        }
        message.setMessage(200, 8, 34, 456);
        message.setMessage(200, 9, 34, 56);
        assertEquals(9, message.getChannel());
        assertEquals(192, message.getCommand());
        assertEquals(34, message.getData1());
        assertEquals(0, message.getData2());
        assertEquals(2, message.getLength());
        assertEquals(201, message.getStatus());
        assertEquals(2, message.getMessage().length);
        message.setMessage(148, 9, 34, 56);
        assertEquals(9, message.getChannel());
        assertEquals(144, message.getCommand());
        assertEquals(34, message.getData1());
        assertEquals(56, message.getData2());
        assertEquals(3, message.getLength());
        assertEquals(153, message.getStatus());
        assertEquals(3, message.getMessage().length);
    }
