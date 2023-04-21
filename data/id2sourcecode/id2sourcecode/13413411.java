    protected void serializeComment(XMLStreamReader reader, XMLStreamWriter writer) throws XMLStreamException {
        writer.writeComment(reader.getText());
    }
