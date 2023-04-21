    @Test
    public void testGetAttributeChannels2() {
        AttributeDefinition definition = new AttributeDefinition("", "4,5");
        Attribute attribute = new Attribute(definition);
        assertEquals(attribute.getChannelCount(), 2);
        assertEquals(attribute.getChannel(0).getOffset(), 4);
        assertEquals(attribute.getChannel(1).getOffset(), 5);
    }
