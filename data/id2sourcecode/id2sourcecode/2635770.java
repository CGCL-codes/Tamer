    public void test_clone2() {
        byte[] bt = new byte[] { 1, 2, 3, 4, 5 };
        ShortMessage1 message = new ShortMessage1(bt);
        assertTrue(message.clone() != message);
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
        bt[0] = 10;
        bt[1] = 20;
        bt[2] = 30;
        bt[3] = 40;
        bt[4] = 50;
        assertEquals(10, message.getChannel());
        assertEquals(0, message.getCommand());
        assertEquals(20, message.getData1());
        assertEquals(30, message.getData2());
        assertEquals(10, message.getStatus());
        assertEquals(10, message.getMessage()[0]);
        assertEquals(20, message.getMessage()[1]);
        assertEquals(30, message.getMessage()[2]);
        assertEquals(40, message.getMessage()[3]);
        assertEquals(50, message.getMessage()[4]);
        assertEquals(1, tmessage.getChannel());
        assertEquals(0, tmessage.getCommand());
        assertEquals(2, tmessage.getData1());
        assertEquals(3, tmessage.getData2());
        assertEquals(1, tmessage.getStatus());
        assertEquals(1, tmessage.getMessage()[0]);
        assertEquals(2, tmessage.getMessage()[1]);
        assertEquals(3, tmessage.getMessage()[2]);
        assertEquals(4, tmessage.getMessage()[3]);
        assertEquals(5, tmessage.getMessage()[4]);
    }
