    public void test_clone1() {
        ShortMessage message = new ShortMessage();
        assertTrue(message.clone() != message);
        assertEquals(message.clone().getClass(), message.getClass());
        ShortMessage tmessage;
        tmessage = (ShortMessage) message.clone();
        assertEquals(message.getLength(), tmessage.getLength());
        assertEquals(message.getMessage().length, tmessage.getMessage().length);
        assertEquals(message.getData1(), tmessage.getData1());
        assertEquals(message.getData2(), tmessage.getData2());
        assertEquals(message.getChannel(), tmessage.getChannel());
        assertEquals(message.getCommand(), tmessage.getCommand());
        assertEquals(message.getStatus(), tmessage.getStatus());
        if (message.getMessage().length != 0) {
            for (int i = 0; i < message.getMessage().length; i++) {
                assertEquals(message.getMessage()[i], tmessage.getMessage()[i]);
            }
        }
    }
