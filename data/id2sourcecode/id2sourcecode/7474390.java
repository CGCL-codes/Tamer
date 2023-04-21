    private void initAttributes() {
        for (int i = 0; i < definition.getAttributeCount(); i++) {
            AttributeDefinition attributeDefinition = definition.getAttribute(i);
            Attribute attribute = new Attribute(attributeDefinition);
            attributes.add(attribute);
            for (int j = 0; j < attribute.getChannelCount(); j++) {
                FixtureChannel channel = attribute.getChannel(j);
                channelMap.put(channel.getName(), channel);
            }
        }
    }
