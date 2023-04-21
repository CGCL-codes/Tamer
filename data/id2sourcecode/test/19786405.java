    public static void copy(XMLStreamReader reader, XMLStreamWriter writer) throws XMLStreamException {
        int read = 0;
        int event = reader.getEventType();
        while (reader.hasNext()) {
            switch(event) {
                case XMLStreamConstants.START_ELEMENT:
                    read++;
                    writeStartElement(reader, writer);
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    writer.writeEndElement();
                    read--;
                    if (read <= 0) return;
                    break;
                case XMLStreamConstants.CHARACTERS:
                    writer.writeCharacters(reader.getText());
                    break;
                case XMLStreamConstants.START_DOCUMENT:
                case XMLStreamConstants.END_DOCUMENT:
                case XMLStreamConstants.ATTRIBUTE:
                case XMLStreamConstants.NAMESPACE:
                    break;
                case XMLStreamConstants.CDATA:
                    writer.writeCData(reader.getText());
                    break;
                default:
                    break;
            }
            event = reader.next();
        }
    }
