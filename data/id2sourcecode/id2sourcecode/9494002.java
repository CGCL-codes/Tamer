    public static void copy(XMLStreamReader reader, XMLStreamWriter writer) {
        {
            Assert.notNull(reader, "XMLStreamReader MUST be different from NULL");
            Assert.notNull(writer, "XMLStreamWriter MUST be different from NULL");
        }
        try {
            XMLStreamUtils.copy(reader, writer);
        } catch (XMLStreamException e) {
            throw new XmlException(e);
        }
    }
