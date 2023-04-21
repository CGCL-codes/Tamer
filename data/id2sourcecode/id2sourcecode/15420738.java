    private void marshallAttributes(final HierarchicalStreamWriter writer) {
        if (fixtureDefinition.getAttributeCount() > 0) {
            writer.startNode("attributes");
            for (int i = 0; i < fixtureDefinition.getAttributeCount(); i++) {
                AttributeDefinition attribute = fixtureDefinition.getAttribute(i);
                writer.startNode("attribute");
                writer.addAttribute("name", attribute.getName());
                writer.addAttribute("channels", attribute.getChannels());
                marshalAttributeValues(attribute, writer);
                writer.endNode();
            }
            writer.endNode();
        }
    }
