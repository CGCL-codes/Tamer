    public void testConstructor() {
        CayenneContext context = new CayenneContext();
        assertNotNull(context.getGraphManager());
        assertNull(context.getChannel());
        MockDataChannel channel = new MockDataChannel();
        context.setChannel(channel);
        assertSame(channel, context.getChannel());
    }
