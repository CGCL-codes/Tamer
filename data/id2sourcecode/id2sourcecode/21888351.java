    public void testConstructors() {
        DataChannel handler1 = new MockDataChannel();
        LocalConnection connector1 = new LocalConnection(handler1);
        assertFalse(connector1.isSerializingMessages());
        assertSame(handler1, connector1.getChannel());
        DataChannel handler2 = new MockDataChannel();
        LocalConnection connector2 = new LocalConnection(handler2, LocalConnection.JAVA_SERIALIZATION);
        assertTrue(connector2.isSerializingMessages());
        assertSame(handler2, connector2.getChannel());
    }
