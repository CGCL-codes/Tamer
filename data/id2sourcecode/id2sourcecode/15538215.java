    private void checkMessage(MessageObserver handler, Message m) throws Exception {
        assertNull(handler.getCurrentMessage());
        sendToUDP(m);
        Message read = handler.getLastMessage(100);
        assertNotNull(read);
        assertEquals(m.getClass(), read.getClass());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        m.write(out);
        byte[] b1 = out.toByteArray();
        out.reset();
        read.write(out);
        byte[] b2 = out.toByteArray();
        assertEquals(b1, b2);
    }
