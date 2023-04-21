    protected void serializeText(XMLStreamReader reader, XMLStreamWriter writer) throws XMLStreamException {
        writer.writeCharacters(reader.getText());
    }
