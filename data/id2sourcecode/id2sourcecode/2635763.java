    public void test_getChannel() throws Exception {
        ShortMessage message = new ShortMessage();
        assertEquals(0, message.getChannel());
        byte[] bt = new byte[] { 23, 16, 35 };
        ShortMessage1 message1 = new ShortMessage1(bt);
        assertEquals(7, message1.getChannel());
        bt[0] = 15;
        assertEquals(15, message1.getChannel());
        ShortMessage1 message2 = new ShortMessage1(null);
        assertEquals(0, message2.getChannel());
        message.setMessage(249);
        assertEquals(9, message.getChannel());
        message.setMessage(250, 14, 62);
        assertEquals(10, message.getChannel());
        message.setMessage(234, 15, 14, 62);
        assertEquals(15, message.getChannel());
    }
