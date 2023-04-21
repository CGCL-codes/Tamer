    protected void serializeCData(XMLStreamReader reader, XMLStreamWriter writer) throws XMLStreamException {
        writer.writeCData(reader.getText());
    }
