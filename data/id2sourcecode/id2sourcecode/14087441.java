    @Test
    public void initChannels1() {
        AttributeDefinition definition = new AttributeDefinition("", "5");
        Attribute attribute = new Attribute(definition);
        assertEquals(attribute.getChannelCount(), 1);
        assertEquals(attribute.getChannel(0).getOffset(), 5);
    }
