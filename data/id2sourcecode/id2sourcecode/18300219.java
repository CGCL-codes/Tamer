    public void testChannel() {
        MockDataChannel channel = new MockDataChannel();
        CayenneContext context = new CayenneContext(channel);
        assertSame(channel, context.getChannel());
    }
