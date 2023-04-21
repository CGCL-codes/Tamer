    @Test
    public void testGetMaxChannelNumber() {
        AttributeDefinition definition = new AttributeDefinition("", "4,5");
        Attribute attribute = new Attribute(definition);
        assertEquals(attribute.getMaxChannelNumber(), 0);
        attribute.getChannel(0).setNumber(1);
        attribute.getChannel(1).setNumber(2);
        assertEquals(attribute.getMaxChannelNumber(), 2);
    }
